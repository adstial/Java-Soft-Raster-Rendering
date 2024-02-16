package code.mygL;


import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


import java.awt.image.DataBufferInt;
import code.dependence.utils.Box;

public class ScreenBuffer {
    private int x, y, width, height;
    private final JPanel panel;
    private boolean AorB;
    private Box<BufferedImage> nowBufferImage;
    private Box<int []> nowScreen;
    private Box<float []> nowZBuffer;
    private Box<int []> screenA, screenB;
    private Box<float[]> zBufferA, zBufferB;
    private Box<BufferedImage> bufferedImageA, bufferedImageB;
    private int backgroundColor;

    public ScreenBuffer(int x, int y, int width, int height, final WindowApplication context) {
        this.x = x; this.y = y; this.width = width; this.height = height;
        panel = (JPanel) context.getFrame().getContentPane();
        panel.setPreferredSize(new Dimension(width, height));
        panel.setMinimumSize(new Dimension(width,height));
        panel.setLocation(x, y);
        panel.setVisible(true);

        bufferedImageA = new Box<>(new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB));
        var destA = bufferedImageA.getT().getRaster().getDataBuffer();
        screenA = new Box<>(((DataBufferInt)destA).getData());

        bufferedImageB = new Box<>(new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB));
        var destB = bufferedImageB.getT().getRaster().getDataBuffer();
        screenB = new Box<>(((DataBufferInt)destB).getData());

        zBufferA = new Box<>(new float[width*height]);
        zBufferB = new Box<>(new float[width*height]);

        AorB = true;
        nowBufferImage = bufferedImageA;
        nowScreen = screenA;
        nowZBuffer = zBufferA;
    }
    public void clearBuffer() {
        nowZBuffer.getT()[0] = 0;
        for(int i = 1; i < nowZBuffer.getT().length; i+=i)
            System.arraycopy(nowZBuffer.getT(), 0, nowZBuffer.getT(), i, Math.min(nowZBuffer.getT().length - i, i));

        nowScreen.getT()[0] = backgroundColor;
        for(int i = 1; i < nowScreen.getT().length; i+=i)
            System.arraycopy(nowScreen.getT(), 0, nowScreen.getT(), i, Math.min(nowScreen.getT().length - i, i));
    }
    public void exchange() {
        AorB = !AorB;
        if (AorB) {
            nowScreen = screenA;
            nowBufferImage = bufferedImageA;
            nowZBuffer = zBufferA;
        } else {
            nowScreen = screenB;
            nowBufferImage = bufferedImageB;
            nowZBuffer = zBufferB;
        }
    }
    public void reSet() {
        //TODO
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public JPanel getPanel() {
        return panel;
    }
    public boolean isAorB() {
        return AorB;
    }
    public int[] getNowScreen() {
        return nowScreen.getT();
    }
    public float[] getNowZBuffer() {
        return nowZBuffer.getT();
    }
    public void setNowScreenAll(int number) {
        nowScreen.getT()[0] = number;
        for(int i = 1; i < nowScreen.getT().length; i+=i)
            System.arraycopy(nowScreen.getT(), 0, nowScreen.getT(), i, Math.min(nowScreen.getT().length - i, i));
    }
    public BufferedImage getNowBufferImage() {
        return nowBufferImage.getT();
    }

    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
    }

}
