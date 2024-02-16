package code.mygL;

import code.dependence.math.QuickMath;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.EventListener;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

public class WindowApplication {
    private static WindowApplication context;
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
    private Camera camera;

    public static WindowApplication CreateDefaultMainApplication(String name, int width, int height, int type) {
        var thisWindow = new WindowApplication();
        thisWindow.name = name;
        thisWindow.windowWidth = width; thisWindow.windowHeight = height;
        thisWindow.willClose = false;

        thisWindow.frame = new JFrame(name);
        thisWindow.frame.setSize(width,height);
        thisWindow.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        thisWindow.frame.setResizable(false);

        thisWindow.windowEvent = new LinkedList<>();
        thisWindow.rasterizer = new Rasterizer();
        thisWindow.camera = new Camera();
        return thisWindow;
    }
    public static final int NoneStyle = 0;
    private WindowApplication() {}
    public void initialize() {
        QuickMath.initialize();

        context = this;
        this.screenBuffer =
                new ScreenBuffer(viewPortLocationX, viewPortLocationY, viewPortWidth, viewPortHeight, this);
        frame.setVisible(true);
        rasterizer.setContext(viewPortWidth, viewPortHeight, viewPortWidth * 2 / 3);
        g = screenBuffer.getPanel().getGraphics();

    }
    public boolean checkPreparation() {
        return name != null && frame != null && !willClose &&
                windowWidth != 0 && windowHeight != 0 &&
                viewPortHeight != 0 && viewPortWidth != 0 &&
                windowEvent != null;
    }
    public void updateCamera() {
        camera.update(true, true);
    }
    public void drawScreen() {
        screenBuffer.clearBuffer();
        rasterizer.setContext(screenBuffer.getNowScreen(), screenBuffer.getNowZBuffer(), camera);
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
            e.printStackTrace();
        }
        oldTime = System.currentTimeMillis();
    }
    public void terminate() {
        frame.dispose();
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
    public Camera getCamera() {
        return camera;
    }
    public void addInputListener(EventListener listener) {
        if (listener instanceof KeyListener)
            frame.addKeyListener((KeyListener) listener);
        else if (listener instanceof MouseMotionListener)
            frame.addMouseMotionListener((MouseMotionListener) listener);
    }
    public void setScreenBackgroundColour(int color) {
        screenBuffer.setBackgroundColor(color);
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
    public static WindowApplication getContext() {
        return context;
    }
    public final JFrame getFrame() {
        return frame;
    }
    public void setExpectFps(float fps) {
        expectFps = fps;
        expectMsPerFrame = (int) ((1 / fps) * 1000);
    }
    public void addVBO(VBO vbo) {
        rasterizer.addVBO(vbo);
    }
    public static KeyListener MoveFunction(String type) {
        if (type.equals("Default 1")) {
            return new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    var context = WindowApplication.getContext();
                    var keyCode = e.getKeyCode();
                    if (keyCode == KeyEvent.VK_ESCAPE) context.setShouldClose(true);
                    if (keyCode == KeyEvent.VK_1) {
                        context.getFrame().requestFocus();
                    }
                    var camera = context.getCamera();
                    switch (keyCode) {
                        case KeyEvent.VK_W ->
                                camera.MOVE_FORWARD = true;
                        case KeyEvent.VK_S ->
                                camera.MOVE_BACKWARD = true;
                        case KeyEvent.VK_A ->
                                camera.SLIDE_LEFT = true;
                        case KeyEvent.VK_D ->
                                camera.SLIDE_RIGHT = true;
                        case KeyEvent.VK_UP ->
                            camera.LOOK_UP = true;
                        case KeyEvent.VK_DOWN ->
                            camera.LOOK_DOWN = true;
                        case KeyEvent.VK_LEFT ->
                            camera.LOOK_LEFT = true;
                        case KeyEvent.VK_RIGHT ->
                            camera.LOOK_RIGHT = true;
                    }
                }
                @Override
                public void keyReleased(KeyEvent e) {
                    var keyCode = e.getKeyCode();
                    var camera = WindowApplication.getContext().getCamera();
                    switch (keyCode) {
                        case KeyEvent.VK_W ->
                                camera.MOVE_FORWARD = false;
                        case KeyEvent.VK_S ->
                                camera.MOVE_BACKWARD = false;
                        case KeyEvent.VK_A ->
                                camera.SLIDE_LEFT = false;
                        case KeyEvent.VK_D ->
                                camera.SLIDE_RIGHT = false;
                        case KeyEvent.VK_UP ->
                                camera.LOOK_UP = false;
                        case KeyEvent.VK_DOWN ->
                                camera.LOOK_DOWN = false;
                        case KeyEvent.VK_LEFT ->
                                camera.LOOK_LEFT = false;
                        case KeyEvent.VK_RIGHT ->
                                camera.LOOK_RIGHT = false;
                    }
                }

            };
        }
        return null;
    }
}
