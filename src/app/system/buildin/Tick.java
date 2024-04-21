package app.system.buildin;

import app.top.Schedule;
import app.top.World;

public class Tick implements app.system.System{

    private long last;
    private long now;

    private long delta;

    @Override
    public Schedule getSchedule() {
        return Schedule.First;
    }

    @Override
    public void onInit(World world) {
        now = java.lang.System.currentTimeMillis();
    }

    @Override
    public void onUpdate(World world) {
        last = now;
        now = java.lang.System.currentTimeMillis();
        delta = now - last;
    }

    @Override
    public void onDestroy(World world) {

    }

    public long getDelta() {
        return delta;
    }
}
