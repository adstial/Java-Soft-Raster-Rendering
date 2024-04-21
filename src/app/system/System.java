package app.system;

import app.top.Schedule;
import app.top.World;

import java.util.UUID;

public interface System {

    UUID uid = UUID.randomUUID();

    default UUID getUUID() {
        return uid;
    }

    default boolean isEqual(final System system) {
        return uid.equals(system.getUUID());
    }




    Schedule getSchedule();

    void onInit(final World world);

    void onUpdate(final World world);

    void onDestroy(final World world);
}
