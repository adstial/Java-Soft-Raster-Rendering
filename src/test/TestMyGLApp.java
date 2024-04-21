package test;


import app.plugin.DefaultlPlugin;
import app.startup.MyGLApp;

public class TestMyGLApp {
    public static void main(String[] args) {
        MyGLApp.New()
                .addPlugin(DefaultlPlugin.class)
                .run();
    }

}
