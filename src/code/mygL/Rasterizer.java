package code.mygL;

import java.util.ArrayList;

public class Rasterizer {
    private ArrayList<VBO> vboList;
    public final Shader[] shaders;
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
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }
    public Rasterizer() {
        vboList = new ArrayList<>(1000);
        shaders = new Shader[4];
        for (var i = 0; i < shaders.length; i++) {
            shaders[i] = new Shader("Shader" + i);
            shaders[i].vboList = vboList;
            shaders[i].start();
        }
    }
    public void setContext(int width, int height, int distance) {
        for (var shader: shaders) {
            shader.setContext(width, height, distance);
        }
    }
    public void setContext(int[] screen, float[] zBuffer, Camera camera) {
        for (var shader: shaders) {
            shader.screen = screen;
            shader.zBuffer = zBuffer;
            shader.camera = camera;
        }
    }
    public void addVBO(VBO vbo) {
        vboList.add(vbo);
    }
}
