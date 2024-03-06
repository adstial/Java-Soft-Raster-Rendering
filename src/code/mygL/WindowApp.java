package code.mygL;

import code.dependence.math.QuickMath;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.LinkedList;
import java.util.Queue;

import code.dependence.logger.Logger;

public class WindowApp {
    private static WindowApp context;
    public static final Logger log = Logger.getGlobal();
    private String name;
    private JFrame frame;
    private ScreenBuffer screenBuffer;
    private float expectFps;
    private int expectMsPerFrame;
    private long oldTime;
    private boolean willClose;
    private int windowWidth,windowHeight;
    private int viewPortLocationX, viewPortLocationY;
    private int viewPortWidth, viewPortHeight;

    private Graphics g;
    private Graphics infoG;

    private Queue<String> windowEvent;
    private Rasterizer rasterizer;
    private ArrayList<VBO> vboList;

    public static WindowApp CreateDefaultMainApp(String name, int width, int height, int type) {
        var thisWindow = new WindowApp();
        thisWindow.name = name;
        thisWindow.windowWidth = width; thisWindow.windowHeight = height;
        thisWindow.willClose = false;
        thisWindow.windowEvent = new LinkedList<>();

        thisWindow.frame = new JFrame(name);

        thisWindow.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        thisWindow.frame.setResizable(false);

        context = thisWindow;
        return thisWindow;
    }
    public static final int NoneStyle = 0;
    private WindowApp() {}
    public void initialize() {
        QuickMath.initialize();


        frame.setSize(windowWidth,windowHeight);


        screenBuffer = new ScreenBuffer(viewPortLocationX, viewPortLocationY, viewPortWidth, viewPortHeight, this);
        frame.setVisible(true);

        rasterizer = new Rasterizer();
        rasterizer.setContext(viewPortWidth, viewPortHeight, viewPortWidth * 2 / 3);
        rasterizer.setContext(vboList);
        rasterizer.setLog(log);
        g = screenBuffer.getPanel().getGraphics();

    }
    public boolean checkPreparation() {
        return name != null && frame != null && !willClose &&
                windowWidth != 0 && windowHeight != 0 &&
                viewPortHeight != 0 && viewPortWidth != 0 &&
                windowEvent != null;
    }
    public void updateCamera() {
        Camera.update(true, true);
    }
    public void drawScreen() {
        screenBuffer.clearBuffer();
        rasterizer.setContext(screenBuffer.getNowScreen(), screenBuffer.getNowZBuffer());
        rasterizer.prepare();
        rasterizer.render();
    }
    public void swapBuffer() {
        g.drawImage(screenBuffer.getNowBufferImage(), 0,0,windowWidth, windowHeight, null);
        screenBuffer.exchange();
    }
    public void pollEvents() {
    }
    public void keepFps() {
        if (expectFps <= 0 ) return;
        var nowTime = System.currentTimeMillis();
        var runTime = nowTime - oldTime;
        if (runTime < expectMsPerFrame) try {
            Thread.sleep(expectMsPerFrame - runTime);
        } catch (InterruptedException e) {
            log.fatal(e.toString());
            setShouldClose(true);
        }
        oldTime = System.currentTimeMillis();
    }
    public void terminate() {
        frame.dispose();
        log.waitUntilFinish();
    }
    public void setViewPort(int viewPortLocationX, int viewPortLocationY, int viewPortWidth, int viewPortHeight) {
        this.viewPortLocationX = viewPortLocationX;
        this.viewPortLocationY = viewPortLocationY;
        this.viewPortWidth = viewPortWidth;
        this.viewPortHeight = viewPortHeight;
    }
    public void setViewPortType(String type) {
        if (type.equals("Follow Screen")) {
            this.viewPortLocationX = 0;
            this.viewPortLocationY = 0;
            this.viewPortWidth = windowWidth;
            this.viewPortHeight = windowHeight;
//            this.frame.setResizable(true);
            this.frame.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
//                    var frame = (JFrame) e.getSource();
//                    var width = frame.getWidth();
//                    var height = frame.getHeight();
//                    context.setViewPort(0,0,width, height);
//                    context.screenBuffer.reSet();
                }
            });
        }
    }

    public void setFrameType(final String type) {
        if (type.equals("Center")) {
            var dim = Toolkit.getDefaultToolkit().getScreenSize();
            var screenHeight = dim.height; var screenWidth = dim.width;
            frame.setLocation((screenWidth - windowWidth) / 2, (screenHeight - windowHeight) / 2);
        }
    }

    public void addInputListener(EventListener listener) {
        if (listener instanceof KeyListener)
            frame.addKeyListener((KeyListener) listener);
        else if (listener instanceof MouseMotionListener)
            frame.addMouseMotionListener((MouseMotionListener) listener);
    }
    public void setScreenBackgroundColour(int color) {
        if (screenBuffer != null)
            screenBuffer.setBackgroundColor(color);
        else log.warming(WindowApp.class, "ScreenBuffer has not initialized");
    }
    public boolean ShouldClose() {
        return willClose;
    }
    public void setShouldClose(boolean willClose) {
        this.willClose = willClose;
    }
    public String getName() {
        return name;
    }
    public static WindowApp getContext() {
        return context;
    }
    public final JFrame getFrame() {
        return frame;
    }
    public void setExpectFps(float fps) {
        expectFps = fps;
        expectMsPerFrame = (int) ((1 / fps) * 1000);
    }
    public void setList(ArrayList<VBO> list) {
        if (list == null)
            log.fatal(WindowApp.class, "null value");
        else
            vboList = list;
    }

    public WindowApp setConfig(final ConfigType configType, final Number config) {
        switch (configType) {
            case EXPECT_FPS -> {
                expectFps = config.floatValue();
                expectMsPerFrame = (int) ((1 / expectFps) * 1000);
            }
        }
        return this;
    }
    public WindowApp setConfig(final ConfigType configType, final String config) {
        switch (configType) {
            case FRAME_STYLE -> {
                if (config.equals("center")) {
                    var dim = Toolkit.getDefaultToolkit().getScreenSize();
                    var screenHeight = dim.height;
                    var screenWidth = dim.width;
                    frame.setLocation((screenWidth - windowWidth) / 2, (screenHeight - windowHeight) / 2);
                }
            }
            case INPUT_STYLE -> {
                addInputListener(MoveFunction(config));
            }
            case VIEW_PORT_STYLE -> {
                if (config.equals("follow screen")) {
                    this.viewPortLocationX = 0;
                    this.viewPortLocationY = 0;
                    this.viewPortWidth = windowWidth;
                    this.viewPortHeight = windowHeight;
//            this.frame.setResizable(true);
                    this.frame.addComponentListener(new ComponentAdapter() {
                        @Override
                        public void componentResized(ComponentEvent e) {
//                    var frame = (JFrame) e.getSource();
//                    var width = frame.getWidth();
//                    var height = frame.getHeight();
//                    context.setViewPort(0,0,width, height);
//                    context.screenBuffer.reSet();
                        }
                    });
                }
            }
        }
        return this;
    }

    public static KeyListener MoveFunction(String type) {
        if (type.equals("Default 1")) {
            return new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    var context = WindowApp.getContext();
                    var keyCode = e.getKeyCode();
                    if (keyCode == KeyEvent.VK_ESCAPE) context.setShouldClose(true);
                    if (keyCode == KeyEvent.VK_1) {
                        context.getFrame().requestFocus();
                    }

                    switch (keyCode) {
                        case KeyEvent.VK_W ->
                                Camera.MOVE_FORWARD = true;
                        case KeyEvent.VK_S ->
                                Camera.MOVE_BACKWARD = true;
                        case KeyEvent.VK_A ->
                                Camera.SLIDE_LEFT = true;
                        case KeyEvent.VK_D ->
                                Camera.SLIDE_RIGHT = true;
                        case KeyEvent.VK_UP ->
                                Camera.LOOK_UP = true;
                        case KeyEvent.VK_DOWN ->
                                Camera.LOOK_DOWN = true;
                        case KeyEvent.VK_LEFT ->
                                Camera.LOOK_LEFT = true;
                        case KeyEvent.VK_RIGHT ->
                                Camera.LOOK_RIGHT = true;
                    }
                }
                @Override
                public void keyReleased(KeyEvent e) {
                    var keyCode = e.getKeyCode();
                    switch (keyCode) {
                        case KeyEvent.VK_W ->
                                Camera.MOVE_FORWARD = false;
                        case KeyEvent.VK_S ->
                                Camera.MOVE_BACKWARD = false;
                        case KeyEvent.VK_A ->
                                Camera.SLIDE_LEFT = false;
                        case KeyEvent.VK_D ->
                                Camera.SLIDE_RIGHT = false;
                        case KeyEvent.VK_UP ->
                                Camera.LOOK_UP = false;
                        case KeyEvent.VK_DOWN ->
                                Camera.LOOK_DOWN = false;
                        case KeyEvent.VK_LEFT ->
                                Camera.LOOK_LEFT = false;
                        case KeyEvent.VK_RIGHT ->
                                Camera.LOOK_RIGHT = false;
                    }
                }

            };
        }
        return null;
    }


}
