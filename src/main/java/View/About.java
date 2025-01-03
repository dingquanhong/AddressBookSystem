package View;

import Utils.View.Window;

import javax.swing.*;

public class About {
    private JPanel about;
    private JLabel logo;
    private JTextPane info;
    public About(){
        ImageIcon icon = new ImageIcon("src/main/resources/img/logo.png");
        logo.setIcon(icon);
    }
    public void show() {
        ImageIcon icon = new ImageIcon("src/main/resources/img/logo.png");
        JFrame frame = new JFrame("About");
        frame.setContentPane(new About().about);
        frame.setIconImage(icon.getImage());
        frame.pack();
        Window.tocenter(frame);
        frame.setVisible(true);
    }
}
