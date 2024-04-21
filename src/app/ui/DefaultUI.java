package app.ui;

import app.plugin.Plugin;
import app.startup.MyGLApp;


public class DefaultUI implements Plugin {

    @Override
    public void init(MyGLApp app) {
        app.output = new TopFrame();
    }

    @Override
    public boolean hasExpand() {
        return false;
    }

    @Override
    public Plugin[] getExpand() {
        return null;
    }

    @Override
    public boolean hasSystem() {
        return false;
    }

    @Override
    public System[] getSystem() {
        return null;
    }
}
