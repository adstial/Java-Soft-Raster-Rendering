package app.mygL;

import dependence.utils.Box;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;

public class ScreenBuffer {
    private boolean State;
    public static final boolean A = true;
    public static final boolean B = false;

    private BufferedImage bufferA;
    private BufferedImage bufferB;

    private Box<BufferedImage> currentBuffer = Box.Empty();


    private int[] pixelsA;
    private int[] pixelsB;
    private Box<int[]> currentPixels = Box.Empty();

    public ScreenBuffer(int width, int height) {
        State = A;
        setBuffer(width, height);
        currentBuffer.setT(bufferA);
        currentPixels.setT(pixelsA);
    }

    public void swap() {
        if (State == A) {
            State = B;
            currentBuffer.setT(bufferB);
        } else {
            State = A;
            currentBuffer.setT(bufferA);
        }
    }

    public void setBuffer(int width, int height) {
        bufferA = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        bufferB = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        DataBuffer destA = bufferA.getRaster().getDataBuffer();
        pixelsA = ((DataBufferInt)destA).getData();
        DataBuffer destB = bufferB.getRaster().getDataBuffer();
        pixelsB = ((DataBufferInt)destB).getData();
    }

    public BufferedImage getCurrentBuffer() {
        return currentBuffer.getT();
    }

    public int[] getCurrentPixels() {
        return currentPixels.getT();
    }


}
