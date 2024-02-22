package test.mygL;

import code.dependence.VBO_builder.SimpleVBOBuilder;
import code.dependence.VBO_builder.VBOBuilderFactory;
import code.mygL.VBO;
import org.junit.Test;

import java.util.ArrayList;

import static code.mygL.WindowApplication.CreateDefaultMainApplication;
import static code.mygL.WindowApplication.MoveFunction;
import static code.mygL.WindowApplication.NoneStyle;
import static code.mygL.WindowApplication.log;

public class MainTest {
    @Test
    public void test_WindowApplication() {

        var vboList = new ArrayList<VBO>();
        var builder = VBOBuilderFactory.SetStyle(SimpleVBOBuilder.class);
        var test = builder.setParam(1).build();

        vboList.add(test);

        // Get window
        var name = "Java Soft Raster Rendering";
        var window = CreateDefaultMainApplication(name, 800, 800, NoneStyle);


        // Set config
        window.setExpectFps(60);
        window.setViewPortType("Follow Screen");
        window.addInputListener(MoveFunction("Default 1"));
        window.setList(vboList);


        window.initialize();

        window.setScreenBackgroundColour(0xffffff);

        if (!window.checkPreparation()) {
            log.fatal("window is nor prepared!");
        }

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
