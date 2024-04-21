package app.plugin;

import app.startup.MyGLApp;

import java.util.UUID;

public interface Plugin {
    UUID uid = UUID.randomUUID();

    default UUID getUid() {
        return uid;
    }

    default boolean isEqual(final Plugin plugin) {
        return plugin.getUid().equals(this.getUid());
    }

    void init(final MyGLApp app);

    boolean hasExpand();

    Plugin[] getExpand();

    boolean hasSystem();

    System[] getSystem();
}
