package app.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CycleButton extends JButton {

    private final Color basicColor;
    private final Color hoverColor;

    public CycleButton(final Color color) {
        basicColor = color;
        hoverColor = highlight(color);

        setBackground(basicColor);
        setBorderPainted(false);
        setContentAreaFilled(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(basicColor);
                setCursor(Cursor.getDefaultCursor());
            }
        });
    }

    @Override
    protected void paintComponent(final Graphics g) {
        if (g instanceof Graphics2D g2d) {
            g.setColor(getBackground());
            int diameter = Math.min(getWidth(), getHeight());
            int x = (getWidth() - diameter) / 2;
            int y = (getHeight() - diameter) / 2;

            // 绘制完全圆形按钮
            g2d.fillOval(x, y, diameter, diameter);
        }

        super.paintComponent(g);
    }

    public void setSize(final int radius) {
        setPreferredSize(new Dimension(radius * 2, radius * 2));
    }

    private static Color highlight(final Color color) {
        final var red = Math.min(color.getRed() + 50, 255);
        final var green = Math.min(color.getGreen() + 50, 255);
        final var blue = Math.min(color.getBlue() + 50, 255);

        return new Color(red, green, blue);
    }
}
