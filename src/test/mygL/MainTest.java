package test.mygL;

import code.dependence.VBO_builder.SimpleVBOBuilder;
import code.dependence.VBO_builder.VBOBuilderFactory;
import code.mygL.VBO;
import org.junit.Test;

import java.util.ArrayList;

import static code.mygL.ConfigType.*;
import static code.mygL.WindowApp.CreateDefaultMainApp;
import static code.mygL.WindowApp.NoneStyle;
import static code.mygL.WindowApp.log;


public class MainTest {
    @Test
    public void test_WindowApplication() {

        var vboList = new ArrayList<VBO>();
        var builder = VBOBuilderFactory.SetStyle(SimpleVBOBuilder.class);
        var test1 = builder.setParam(1).build();
        var test2 = builder.setParam(2).build();
        var test3 = builder.setParam(3).build();
        var test4 = builder.setParam(4).build();
//        vboList.add(test1);
//        vboList.add(test2);
//        vboList.add(test3);
        vboList.add(test4);

        // Get window
        var AppName = "Java Soft Raster Rendering";
        var window = CreateDefaultMainApp(AppName, 800, 800, NoneStyle);


        // Set config
        window.setConfig(EXPECT_FPS,60)
                .setConfig(VIEW_PORT_STYLE, "follow screen")
                .setConfig(FRAME_STYLE, "center")
                .setConfig(INPUT_STYLE, "Default 1");

        window.setList(vboList);

        if (!window.checkPreparation()) {
            log.fatal(MainTest.class, "window is nor prepared!");
            System.exit(-1);
        }


        window.initialize();

        window.setScreenBackgroundColour(0xff9999);
        int index = 0;
        while (!window.ShouldClose()) {
//            index++;
//            index %= 60;
//            vboList.get(0).localRotation[0] = index * 2;
//            vboList.get(0).localRotation[1] = index / 2;
            window.updateCamera();
            window.drawScreen();
            window.swapBuffer();
            window.pollEvents();
            window.keepFps();
        }

        window.terminate();
    }

}
