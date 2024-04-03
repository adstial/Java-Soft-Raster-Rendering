package code.ui;

import javax.swing.*;

public class TopFrame extends JFrame {
    public static TopFrame CreateUI() {
        return new TopFrame();
    }


    private JPanel renderPanel;




    private TopFrame() {
        super();
        setSize(800, 600);
        setLocationRelativeTo(null);
        setTitle("MyGL");
        setResizable(false);
        setUndecorated(true);
        setAlwaysOnTop(true);
        setIconImage(null);
        setVisible(true);

    }



    public JPanel getRenderPanel() {
        return renderPanel;
    }

}
