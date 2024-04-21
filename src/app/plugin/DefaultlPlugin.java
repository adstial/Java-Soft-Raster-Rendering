package app.plugin;

import app.startup.MyGLApp;
import app.ui.DefaultUI;

public class DefaultlPlugin implements Plugin{
    @Override
    public void init(MyGLApp app) {
    }

    @Override
    public boolean hasExpand() {
        return true;
    }

    @Override
    public Plugin[] getExpand() {
        return new Plugin[] {
                new DefaultUI()
        };
    }

    @Override
    public boolean hasSystem() {
        return false;
    }

    @Override
    public System[] getSystem() {
        return new System[0];
    }
}
