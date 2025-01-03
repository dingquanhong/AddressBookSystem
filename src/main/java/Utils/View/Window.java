package Utils.View;

import View.GroupDialog;

import javax.swing.*;
import java.awt.*;

public class Window {


    /**
     * 使窗口居中
     *
     * @param frame 窗口
     *
     */
    public static void  tocenter(JFrame frame){

        // 获取主显示器的尺寸
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenX = screenSize.width;
        int screenY = screenSize.height;

        // 获取窗口的尺寸
        int frameWidth = frame.getWidth();
        int frameHeight = frame.getHeight();

        // 如果窗口还没有被显示过，则使用默认的宽度和高度
        if (frameWidth == 0 || frameHeight == 0) {
            frame.pack(); // 对组件进行布局并设置框架大小
            frameWidth = frame.getWidth();
            frameHeight = frame.getHeight();
        }
        // 计算窗口左上角的位置以使其居中
        int x = (screenX - frameWidth) / 2;
        int y = (screenY - frameHeight) / 2;

        // 设置窗口位置
        frame.setLocation(x, y);
    }

    public static void tocenter(GroupDialog dialog) {
        // 获取主显示器的尺寸
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenX = screenSize.width;
        int screenY = screenSize.height;
        int dialogWidth = dialog.getWidth();
        int dialogHeight = dialog.getHeight();
        if (dialogWidth == 0 || dialogHeight == 0) {
            dialog.pack();
            dialogWidth = dialog.getWidth();
            dialogHeight = dialog.getHeight();
        }
        int x = (screenX - dialogWidth) / 2;
        int y = (screenY - dialogHeight) / 2;
        dialog.setLocation(x, y);

    }
}
