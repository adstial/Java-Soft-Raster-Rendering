package code.mygL;

import code.dependence.logger.Logger;

import java.util.ArrayList;

public class Rasterizer {
    private ArrayList<VBO> vboList;
    public final RenderCore[] renderCores;
    private Logger log;
    public void prepare() {
        var num = vboList.size();
        if (num < renderCores.length) {
            renderCores[0].setVboStartAndEnd(0, num - 1);
            for (int i = 1; i < renderCores.length; i++) {
                renderCores[i].setVboStartAndEnd(-1,-1);
            }
        } else {
            var ave = num / renderCores.length;
            
        }
    }
    public void render() {
        for (RenderCore renderCore : renderCores) {
            synchronized (renderCore) {
                renderCore.work = true;
                renderCore.notify();
            }
        }
        for (RenderCore renderCore : renderCores) {
            synchronized (renderCore.lock) {
                while (renderCore.work) {
                    try {
                        renderCore.lock.wait();
                    } catch (InterruptedException e) {
                        log.fatal(Rasterizer.class, e.toString());
                        WindowApplication.getContext().setShouldClose(true);
                    }
                }
            }
        }
    }
    public Rasterizer() {
        renderCores = new RenderCore[4];
        for (var i = 0; i < renderCores.length; i++) {
            renderCores[i] = new RenderCore("RenderCore" + i);
            renderCores[i].start();
        }
    }
    public void setContext(int width, int height, int distance) {
        for (var shader: renderCores) {
            shader.setContext(width, height, distance);
        }
    }
    public void setContext(int[] screen, float[] zBuffer) {
        for (var shader : renderCores) {
            shader.setContext(screen, zBuffer);
        }
    }
    public void setContext(ArrayList<VBO> vboList) {
        if (vboList != null) {
            this.vboList = vboList;
            for (var shader : renderCores) {
                shader.setContext(vboList);
            }
        }
    }

    public void setLog(Logger log) {
        if (this.log == null) {
            this.log = log;
        } else {
            this.log.warming(Rasterizer.class, "reset exited log");
        }
    }
}
