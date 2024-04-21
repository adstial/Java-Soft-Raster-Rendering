package app.top;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface Output {
    boolean hasChanged();
    Dimension getCanvasSize();
    void submit(BufferedImage image);
}
