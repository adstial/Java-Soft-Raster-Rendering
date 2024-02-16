package test.mygL;

import org.junit.Test;

import static code.mygL.WindowApplication.CreateDefaultMainApplication;
import static code.mygL.WindowApplication.MoveFunction;
import static code.mygL.WindowApplication.NoneStyle;

public class TestUtil {
    @Test
    public void test_WindowApplication() {
        // Get window
        var name = "Java Soft Raster Rendering";
        var window = CreateDefaultMainApplication(name, 800, 800, NoneStyle);


        // Set config
        window.setExpectFps(60);
        window.setViewPortType("Follow Screen");
        window.addInputListener(MoveFunction("Default 1"));

        window.initialize();

        window.setScreenBackgroundColour(0xffffff);

        if (!window.checkPreparation())
            return;

        while (!window.ShouldClose()) {
            window.updateCamera();
            window.drawScreen();
            window.swapBuffer();
            window.pollEvents();
            window.keepFps();
        }

        window.terminate();
    }
}
