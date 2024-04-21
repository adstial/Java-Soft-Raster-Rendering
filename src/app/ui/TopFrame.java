package app.ui;

import app.top.Output;
import dependence.ConstPool;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


public class TopFrame extends JFrame implements Output {

    private final Canvas canvas;
    private final Graphics graphics;

    public TopFrame() {

        setSize(ConstPool.SIZE);
        setLocationRelativeTo(null);

        setUndecorated(true);

        final var titleBar = new CustomTitleBar(ConstPool.TITLE, this);
        add(titleBar, BorderLayout.NORTH);

        canvas = new Canvas();

        canvas.setBackground(Color.WHITE);


        add(canvas, BorderLayout.CENTER);

        setResizable(true);
        setBackground(Color.GRAY);
        setVisible(true);

        graphics = canvas.getGraphics();
    }

    @Override
    public boolean hasChanged() {
        return canvas.hasChanged;
    }

    @Override
    public Dimension getCanvasSize() {
        return canvas.getSize();
    }

    @Override
    public void submit(BufferedImage image) {
        graphics.drawImage(image, 0, 0, null);
    }
}
