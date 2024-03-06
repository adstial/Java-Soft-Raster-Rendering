package test.app;

import code.app.AppConfigType;
import org.junit.Test;

import static code.app.MyGLApp.CreateMyGLApp;

public class TestApp {
    @Test
    public void testMyGLApp() {
        CreateMyGLApp("test")
                .setParam(AppConfigType.DefaultConfig) // set config
                .run(); // run app
    }
}
