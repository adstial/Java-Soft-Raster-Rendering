package app.startup;

import app.mygL.MyGL;
import app.plugin.Plugin;
import app.plugin.PluginManager;
import app.top.Output;
import app.top.World;
import dependence.ConstPool;

import java.util.ArrayList;

public class MyGLApp {
    private static MyGLApp Context;         // 当前上下文

    public MyGLApp() {
        world = new World();
        gl = new MyGL();
        manager = new PluginManager();
        pluginDepth = 0;
    }

    public static MyGLApp New() {
        Context = new MyGLApp();

        Context.tempPlugins = new ArrayList<>();

        return Context;
    }

    public PluginManager manager;

    public Output output;
    public MyGL gl;
    public boolean readyToRender;

    private ArrayList<Class<? extends Plugin>> tempPlugins;

    private int pluginDepth;

    private final World world;

    public volatile boolean willEnd;


    public void run() {

        prepare();

        world.setContext(Context);

        Thread worldThread = new Thread(world);
        worldThread.start();

        gl.setSize(ConstPool.SIZE.width, ConstPool.SIZE.height, null);
        gl.setOutput(output);
        Thread glThread = new Thread(gl);
        glThread.start();

        while (!willEnd) {
            Thread.onSpinWait();
        }
    }



    private void prepare() {
        if (pluginDepth != 0) {
            throw new RuntimeException("pluginDepth != 0");
        }

        manager.load(tempPlugins);

        tempPlugins = null;
    }

    public MyGLApp addPlugin(final Class<? extends Plugin> clazz) {

        pluginDepth += 1;

        tempPlugins.add(clazz);

        pluginDepth -= 1;

        return this;
    }


    public static MyGLApp getContext() {
        return Context;
    }

}
