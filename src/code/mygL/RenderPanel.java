package code.mygL;

import code.app.AppConfig;
import code.dependence.logger.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class RenderPanel extends JPanel {
    public static final Logger log = Logger.getGlobal();
    private final RenderBuffer renderBuffer;

    private BufferedImage bufferedImage;

    private Graphics graphics;

    private final int width, height;


    private int backgroundColor;

    public RenderPanel() {

        setLocation(AppConfig.px, AppConfig.py);

        this.width = AppConfig.pw;
        this.height = AppConfig.ph;
        setPreferredSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));

        setVisible(true);

        renderBuffer = new RenderBuffer(width, height);

    }

    public void setGraphics() {
        graphics = getGraphics();
    }

    public boolean hasPrepared() {
        return renderBuffer != null &&
                width != 0 && height != 0;
    }

    public final void clearBuffer() {
        renderBuffer.clearBuffer();
    }

    public final void setBuffer() {
        bufferedImage = renderBuffer.getNowBufferImage();
    }

    public final void swapBuffer() {
        graphics.drawImage(bufferedImage, 0, 0, width, height, null);
        renderBuffer.exchange();
    }



    public RenderBuffer getRenderBuffer() {
        return renderBuffer;
    }
}
