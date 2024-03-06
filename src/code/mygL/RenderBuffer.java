package code.mygL;

import code.dependence.utils.Box;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class RenderBuffer {
    private int width, height;
    private boolean AorB;
    private Box<BufferedImage> nowBufferImage;
    private Box<int []> nowScreen;
    private Box<float []> nowZBuffer;
    private Box<int []> screenA, screenB;
    private Box<float[]> zBufferA, zBufferB;
    private Box<BufferedImage> bufferedImageA, bufferedImageB;
    private int backgroundColor;

    public RenderBuffer(int width, int height) {
        this.width = width; this.height = height;

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

    public boolean AorB() {
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
