package code.app;

import code.dependence.logger.Logger;
import code.mygL.*;

import java.util.ArrayList;

public class RenderThread extends Thread {

    private Logger log = Logger.getGlobal();


    private RenderPanel renderPanel;

    private boolean running;


    public boolean fpsControl;
    private float expectFps;
    private int expectMsPerFrame;


    private ArrayList<VBO> vboList;
    private int vboNumber;
    private RenderCore[] renderCores;

    private long oldTime;




    @Override
    public void run() {

        renderPanel.setGraphics();

        renderPanel.getRenderBuffer().setBackgroundColor(0xff99ff);

        while (running) {
            Camera.update(true, true);

            // 清除上一帧屏幕缓冲、Z深度缓冲
            renderPanel.clearBuffer();

            // 获得当前帧屏幕缓冲、Z深度缓冲
            renderPanel.setBuffer();

            // 设置环境
            setContext();

            // 为每个RenderCore分配任务
            assignTasks();

            // 启动RenderCore，并等待渲染完成
            renderImage();

            // 交换缓冲，将图像输出到屏幕
            renderPanel.swapBuffer();

            // 控制帧率
            if (fpsControl) {
                var nowTime = System.currentTimeMillis();
                var runTime = nowTime - oldTime;
                if (runTime < expectMsPerFrame) try {
                    Thread.sleep(expectMsPerFrame - runTime);
                } catch (InterruptedException e) {
                    log.fatal(RenderThread.class, e.toString());
                    e.printStackTrace(); // todo
                }
                oldTime = System.currentTimeMillis();
            }
        }
    }

    // 平均分配vbo给每一个render core：
    private void assignTasks() {
        var num = vboList.size();
        if (num == vboNumber) return;
        vboNumber = num;
        if (num < renderCores.length) {
            renderCores[0].setVboStartAndEnd(0, num - 1);
            for (int i = 1; i < renderCores.length; i++) {
                renderCores[i].setVboStartAndEnd(-1,-1);
            }
        } else {
            var quotient = num / renderCores.length;
            var remainder = num % renderCores.length;

            var start = 0; var end = 0;
            for (int i = 0; i < renderCores.length; i++) {
                end = start + quotient + (i < remainder ? 1 : 0);
                renderCores[i].setVboStartAndEnd(start, end - 1);
                start = end;
            }
        }
    }

    private void renderImage() {
        for (var core: renderCores) {
            synchronized (core) {
                core.work = true;
                core.notify();
            }
        }
        for (var core: renderCores) {
            synchronized (core.lock) {
                while (core.work) {
                    try {
                        core.lock.wait();
                    } catch (InterruptedException e) {
                        log.fatal(RenderThread.class, e.toString());
                    }
                }
            }
        }
    }


    public void addPanel(final RenderPanel renderPanel) {
        this.renderPanel = renderPanel;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setExpectFps(float fps) {
        fpsControl = !(fps <= 0);
        this.expectFps = fps;
        try {
            expectMsPerFrame = (int) (1f / expectFps * 1000);
            log.info(RenderThread.class, "fps: " + expectFps);
        } catch (ArithmeticException ignored) {
        }
    }

    public void setRenderCoreNumber(int coreNumber) {
        log.info(RenderThread.class, "creat " + coreNumber + " cores");
        renderCores = new RenderCore[coreNumber];
        for (int i = 0; i < coreNumber; i++) {
            renderCores[i] = new RenderCore("core" + i);
            renderCores[i].start();
        }
    }

    public void setVboList(final ArrayList<VBO> list) {
        vboList = list;
        for (var core: renderCores) {
            core.setContext(vboList);
        }
    }

    private void setContext() {
        var rb = renderPanel.getRenderBuffer();
        for (var core: renderCores) {
            core.setContext(rb.getNowScreen(), rb.getNowZBuffer());
            core.setContext(AppConfig.pw, AppConfig.ph, AppConfig.distance);
        }
    }

}
