package code.mygL;

import code.dependence.logger.Logger;

import java.util.ArrayList;

public class Rasterizer {
    private ArrayList<VBO> vboList;
    public final Shader[] shaders;
    private Logger log;
    public void prepare() {
        var num = vboList.size();
        if (num < shaders.length) {
            shaders[0].setVboStartAndEnd(0, num - 1);
            for (int i = 1; i < shaders.length; i++) {
                shaders[i].setVboStartAndEnd(-1,-1);
            }
        } else {
            var ave = num / shaders.length;
            
        }
    }
    public void render() {
        for (Shader shader : shaders) {
            synchronized (shader) {
                shader.work = true;
                shader.notify();
            }
        }
        for (Shader shader : shaders) {
            synchronized (shader.lock) {
                while (shader.work) {
                    try {
                        shader.lock.wait();
                    } catch (InterruptedException e) {
                        log.fatal(Rasterizer.class, e.toString());
                        WindowApplication.getContext().setShouldClose(true);
                    }
                }
            }
        }
    }
    public Rasterizer() {
        shaders = new Shader[4];
        for (var i = 0; i < shaders.length; i++) {
            shaders[i] = new Shader("Shader" + i);
            shaders[i].start();
        }
    }
    public void setContext(int width, int height, int distance) {
        for (var shader: shaders) {
            shader.setContext(width, height, distance);
        }
    }
    public void setContext(int[] screen, float[] zBuffer) {
        for (var shader : shaders) {
            shader.setContext(screen, zBuffer);
        }
    }
    public void setContext(ArrayList<VBO> vboList) {
        if (vboList != null) {
            this.vboList = vboList;
            for (var shader : shaders) {
                shader.vboList = this.vboList;
            }
        }
    }

    public void setContext(Camera camera) {
        if (camera != null) {
            for (var shader: shaders) {
                shader.setContext(camera);
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
