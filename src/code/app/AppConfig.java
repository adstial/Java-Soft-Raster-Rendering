package code.app;

import code.mygL.Camera;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AppConfig {
    public static AppConfigType acfg;
    public static String appName = "appName";
    public static int width = 1000;
    public static int height = 800;
    public static int x = (Toolkit.getDefaultToolkit().getScreenSize().width - width) / 2;
    public static int y = (Toolkit.getDefaultToolkit().getScreenSize().height - height) / 2;
    public static float expectFps = 60;
    public static int expectMsPerFrame = 16;
    public static KeyListener keyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            System.out.println(e.getKeyCode());
            switch (e.getKeyCode()) {
                // 强制退出
                case KeyEvent.VK_ESCAPE -> System.exit(0);

                // 前进
                case KeyEvent.VK_W -> Camera.MOVE_FORWARD = true;

                // 后退
                case KeyEvent.VK_S -> Camera.MOVE_BACKWARD = true;

                // 左移
                case KeyEvent.VK_A -> Camera.SLIDE_LEFT = true;

                // 右移
                case KeyEvent.VK_D -> Camera.SLIDE_RIGHT = true;

                // 左转
                case KeyEvent.VK_LEFT -> Camera.LOOK_LEFT = true;

                // 右转
                case KeyEvent.VK_RIGHT -> Camera.LOOK_RIGHT = true;

                // 抬头
                case KeyEvent.VK_UP -> Camera.LOOK_UP = true;

                // 低头
                case KeyEvent.VK_DOWN -> Camera.LOOK_DOWN = true;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                // 前进
                case KeyEvent.VK_W -> Camera.MOVE_FORWARD = false;

                // 后退
                case KeyEvent.VK_S -> Camera.MOVE_BACKWARD = false;

                // 左移
                case KeyEvent.VK_A -> Camera.SLIDE_LEFT = false;

                // 右移
                case KeyEvent.VK_D -> Camera.SLIDE_RIGHT = false;

                // 左转
                case KeyEvent.VK_LEFT -> Camera.LOOK_LEFT = false;

                // 右转
                case KeyEvent.VK_RIGHT -> Camera.LOOK_RIGHT = false;

                // 抬头
                case KeyEvent.VK_UP -> Camera.LOOK_UP = false;

                // 低头
                case KeyEvent.VK_DOWN -> Camera.LOOK_DOWN = false;
            }
        }
    };
    public static MouseListener mouseListener = new MouseAdapter() {

    };
    public static MouseMotionListener mouseMotionListener = null;
    public static int px = 0, py = 0;
    public static int pw = 1000, ph = 800;
    public static int distance = 400;
    public static int coreNumber = 10;
}
