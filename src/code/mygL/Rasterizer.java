package code.mygL;

import code.dependence.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Rasterizer {
    private ArrayList<VBO> vboList;
    private int vboNumber;
    public final RenderCore[] renderCores;
    private Logger log;
    public void prepare() {
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
                        WindowApp.getContext().setShouldClose(true);
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

    public boolean hasPrepared() {
        return vboList != null && Arrays.stream(this.renderCores).allMatch(Objects::nonNull);
    }
}
