package app.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CustomTitleBar extends JPanel {
    private boolean isMoving;
    private final Point dis = new Point();
    private final JLabel title;

    public CustomTitleBar(final String titleText, final JFrame frame) {
        setBackground(Color.GRAY);
        setLayout(new TitleBarLayoutManager());

        title = new JLabel(titleText);
        title.setForeground(Color.BLACK);
        title.setFont(new Font("微软雅黑", Font.BOLD, 16));
        title.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                title.setText("Drag to Move");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                title.setText(titleText);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                isMoving = true;
                dis.x = e.getX();
                dis.y = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isMoving = false;
            }
        });

        title.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isMoving) {
                    Point p = frame.getLocation();
                    frame.setLocation(p.x + e.getX() - dis.x, p.y + e.getY() - dis.y);
                }
            }
        });
        title.setVisible(true);
        add(title);

        JButton minimize = new CycleButton(new Color(150, 150, 150));
        minimize.setLocation(0, 0);
        minimize.addActionListener(e -> {
            final var f = (JFrame) SwingUtilities.getWindowAncestor(this);
            f.setState(Frame.ICONIFIED);
        });

        JButton close = new CycleButton(new Color(0xcc0000));
        close.addActionListener(e -> {
            System.exit(0);
        });

        add(close);
        add(minimize);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(super.getWidth(), 30); // 设置标题栏高度为30px
    }

    static class TitleBarLayoutManager implements LayoutManager {
        @Override
        public void addLayoutComponent(String name, Component comp) {

        }

        @Override
        public void removeLayoutComponent(Component comp) {

        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return null;
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return null;
        }

        @Override
        public void layoutContainer(Container parent) {
            int x = 5;
            final var y = 5;
            for (final var comp: parent.getComponents()) {
                if (comp instanceof CycleButton button) {
                    button.setSize(18, 18);
                    button.setLocation(x, y);
                    x += button.getWidth() + 5;
                } else if (comp instanceof JLabel label) {
                    final var parentSize = parent.getSize();
                    final var labelSize = label.getPreferredSize();
                    // 居中
                    label.setSize(labelSize.width, labelSize.height);
                    label.setLocation(parentSize.width / 2 - labelSize.width / 2, y);
                }
            }
        }
    }

}
