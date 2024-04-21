package app.top;

import java.util.Vector;
import app.startup.*;


public class World implements Runnable{
    private MyGLApp app;
    private Schedule schedule;
    private int index;
    private Vector<Entity> entities;

    private volatile boolean alive;

    // 是否阻塞
    public volatile boolean State;
    public static boolean blocking = false;
    public static final boolean running = true;

    // 是否严格同步
//    public volatile boolean strictSync;

    public World() {
        schedule = Schedule.New();
    }

    public void setContext(final MyGLApp app) {
        this.app = app;
    }


    @Override
    public void run() {
        while (!app.willEnd) {






            // 将阻塞
            renderIfPrepared();
        }
    }

    private void renderIfPrepared() {
        if (app.readyToRender) {
            blocking = true;
            while (blocking) {
                Thread.onSpinWait();
            }
        }
    }
}
