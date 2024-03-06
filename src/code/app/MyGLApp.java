package code.app;

import code.dependence.VBO_builder.SimpleVBOBuilder;
import code.dependence.VBO_builder.VBOBuilderFactory;
import code.dependence.logger.Logger;
import code.mygL.EntityManager;
import code.mygL.Rasterizer;
import code.mygL.RenderPanel;
import code.mygL.WindowApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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


        useConfig();
        return this;
    }

    private void useConfig() {
        appName = AppConfig.appName;

        renderThread = new RenderThread();
        renderThread.setExpectFps(AppConfig.expectFps);
        renderThread.setRenderCoreNumber(AppConfig.coreNumber);

        var builder = VBOBuilderFactory.SetStyle(SimpleVBOBuilder.class);
        var test = builder.setParam(1).build();

        EntityManager.addVBO(test);
        renderThread.setVboList(EntityManager.vboList);

    }



    public void run() {

        // 配置遗漏停止运行
        if (!prepared) {
            log.fatal(MyGLApp.class, "NOT PREPARE");
            return;
        }

        // 创建UI
        createUI();


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
    private void createUI() {

        var topFrame = new JFrame(AppConfig.appName);
        topFrame.setSize(AppConfig.width, AppConfig.height);
        topFrame.setLocation(AppConfig.x, AppConfig.y);
        topFrame.setResizable(false);
        topFrame.addKeyListener(AppConfig.keyListener);

        renderPanel = new RenderPanel(AppConfig.pw, AppConfig.ph);
        renderPanel.setGraphics();

        var con = topFrame.getContentPane();
        con.add(renderPanel);


        topFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        topFrame.setVisible(true);


    }


    public static MyGLApp getContext() {
        return context;
    }

}
