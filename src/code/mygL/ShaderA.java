package code.mygL;

import java.util.ArrayList;

public class ShaderA extends Thread{
    public static final float nearClipDistance = 0.01f;
    public final Object lock;
    public boolean work;
    public ArrayList<VBO> vboList;
    public int width, height, halfWidth, halfHeight, distance;
    public int[] screen;
    public float[] zBuffer;
    public Camera camera;
    public int VboStart, VboEnd;
    public VBO vbo;
    public int type;


    public ShaderA(String name) {
        setName(name);
        lock = new Object();


    }


    @Override
    public void run() {
        while (true) {
            sync();

        }
    }


    private void sync() {
        synchronized (this) {
            try {
                synchronized (lock) {
                    lock.notify();
                    work = false;
                }
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void setContext(int width, int height, int distance) {
        this.width = width; halfWidth = width / 2;
        this.height = height; halfHeight = height / 2;
//        xLeft = new int[height]; xRight = new int[height];
//        zLeft = new float[height]; zRight = new float[height];
//        lightLeft = new float[height]; lightRight = new float[height];
        this.distance = distance;
    }
    public void setVboStartAndEnd(int start, int end) {
        this.VboStart = start; this.VboEnd = end;
    }
}
