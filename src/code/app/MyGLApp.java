package code.app;

import code.dependence.VBO_builder.FileVBOBuilder;
import code.dependence.VBO_builder.FunctionVBOBuilder;
import code.dependence.VBO_builder.SimpleVBOBuilder;
import code.dependence.VBO_builder.VBOBuilderFactory;
import code.dependence.logger.Logger;
import code.mygL.EntityManager;
import code.mygL.RenderPanel;
import code.mygL.RenderThread;

import javax.swing.*;

public class MyGLApp {

    // 正在运行的App的上下文
    private static MyGLApp context;

    // App Name
    public String appName;

    // 日志0.
    private Logger log;

    // 继续运行
    private volatile boolean shouldContinue;

    // 未完成时无法运行
    private boolean prepared;

    // 渲染画面panel
    private RenderPanel renderPanel;

    // 渲染线程
    private RenderThread renderThread;

    // 只能通过CreateMyGLApp(final String appName);创建
    private MyGLApp() {}

    // 创建一个App
    public static MyGLApp CreateMyGLApp(final String appName) {
        var app  = new MyGLApp();
        app.appName = appName;
        app.log = Logger.getGlobal();
        app.prepared = false;
        context = app;
        return app;
    }

    // 设置参数、配置
    public MyGLApp setParam(final AppConfigType act) {

        // 防止覆盖原先的配置
        if (prepared) {
            log.warming("recover config");
            return this;
        }

        // 默认配置
        if (act == AppConfigType.DefaultConfig) {
            prepared = true;
            shouldContinue = true;
        } else {
            log.warming(MyGLApp.class, "TODO ...");
        }


        initialize();
        return this;
    }

    private void initialize() {

        // 创建RenderThread
        renderThread = new RenderThread();



        // 加载VBO
        var builder1 = VBOBuilderFactory.SetStyle(SimpleVBOBuilder.class);
//        var builder2 = VBOBuilderFactory.SetStyle(FileVBOBuilder.class);

        var test1 = builder1.setParam(4).build();
        var test2 = builder1.setParam(2).build();
//        var test2 = builder2.setParam("").build();

        EntityManager.addVBO(test1);
        EntityManager.addVBO(test2);
        renderThread.setVboList(EntityManager.vboList);

    }



    public void run() {

        // 配置遗漏停止运行
        if (!prepared) {
            log.fatal(MyGLApp.class, "NOT PREPARE");
            return;
        }

        // 创建UI
        initializeUI();


        if (renderPanel != null && renderPanel.hasPrepared())
            renderThread.addPanel(renderPanel);
        else {
            log.fatal(MyGLApp.class, "render panel has not prepared");
            return;
        }
        renderThread.setRunning(true);
        renderThread.start();
        while (shouldContinue) {
            Thread.onSpinWait();
        }
    }

    // 创建UI
    private void initializeUI() {
        // 主窗口
        var topFrame = new JFrame(AppConfig.appName);
        topFrame.setSize(AppConfig.width, AppConfig.height);
        topFrame.setLocation(AppConfig.x, AppConfig.y);
        topFrame.setResizable(false);
        topFrame.addKeyListener(AppConfig.keyListener);


        renderPanel = new RenderPanel();
        topFrame.add(renderPanel);


        topFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        topFrame.setVisible(true);


    }


    public static MyGLApp getContext() {
        return context;
    }

}
