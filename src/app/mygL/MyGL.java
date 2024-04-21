package app.mygL;

import app.startup.MyGLApp;
import app.top.Output;

import java.awt.*;
import java.sql.SQLOutput;
import java.util.function.Function;

public class MyGL implements Runnable{

    public boolean prepared = false;
    public volatile boolean blocking;

    private int width, height, distance;
    private Output output;


    private Function<Dimension, Integer> distanceFun =  (d) -> (Math.max(d.height, d.width) * 2 / 3);

    public void setSize(int width, int height, final Function<Dimension, Integer> distanceFun) {
        if (prepared) return;

        if (width <= 0 || height <= 0) {
            throw new RuntimeException("MyGL size must be greater than 0");
        }

        this.width = width;
        this.height = height;
        if (distanceFun != null) {
            this.distanceFun = distanceFun;
        }


        assert distanceFun != null;
        this.distance = distanceFun.apply(new Dimension(width, height));

        if (output != null) {
            prepared = true;
        }

    }

    public void setOutput(final Output output) {
        this.output = output;

        if (width != 0 && height != 0 && distance != 0) {
            prepared = true;
        }
    }

    @Override
    public void run() {

        if (!prepared) {
            throw new RuntimeException("MyGL is not prepared");
        }

        while (!MyGLApp.getContext().willEnd) {
            while (blocking) {
                Thread.onSpinWait();
            }

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 模拟渲染耗时
            System.out.println("MyGL is rendering");


            blocking = true;
        }
    }

    public void reSize(final int width, final int height) {
        this.width = width;
        this.height = height;
        this.distance = distanceFun.apply(new Dimension(width, height));
    }

}
