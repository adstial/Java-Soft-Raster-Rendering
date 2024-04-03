package code.app;

import code.app.plugin.top.Plugin;
import code.app.plugin.top.PluginAnnotation;
import code.dependence.logger.Logger;
import code.ui.TopFrame;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class MyGLApp {

    private static MyGLApp context;     // 正在运行的上下文

    private MyGLApp() {
        context = this;
    }


    public static MyGLApp getContext() {    // 获取当前上下文
        return context;
    }

    public static void main(String[] args) {
        CreateMyGLApp().run();

    }


    public static MyGLApp CreateMyGLApp() {     // 创建上下文
        return new MyGLApp();
    }


    /*---------------------------成员变量---------------------------------*/
    private TopFrame topFrame;

    private void initialize() {
        topFrame = TopFrame.CreateUI();
        Logger.Initialize();
    }

    public<T> void addPlugin(final Class<T> clazz) {
        if (!clazz.isAnnotationPresent(Plugin.class)) {
            throw new RuntimeException("Class " + clazz.getName() + " is not a plugin");
        }

        var annotation = clazz.getDeclaredAnnotations();

        Arrays.stream(annotation)
                .map(Annotation::getClass)
                .filter(cl -> cl.isAnnotationPresent(PluginAnnotation.class))
                .forEach(c -> {
                    var pluginAnnotation = c.getAnnotation(PluginAnnotation.class);
                    var pluginType = pluginAnnotation.value();

                });
    }

    public void run() {
        initialize();

        while (true) {

        }
    }


}
