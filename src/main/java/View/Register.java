package View;

import Service.UserService;
import Utils.Database.Result;
import Utils.View.Window;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Register {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JButton registerBtn;
    private JPanel registerFrame;
    static JFrame frame;

    public Register() {
        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserService userService = new UserService();
                if (usernameField.getText().equals("") || passwordField.getText().equals("") || emailField.getText().equals("")) {
                    JOptionPane.showMessageDialog(frame, "请将信息填写完整", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Result result = userService.register(usernameField.getText(), passwordField.getText(), emailField.getText());
                if (result.getStatus()){
                    close();
                    JOptionPane.showMessageDialog(frame, "注册成功!","Success",JOptionPane.INFORMATION_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(frame,result.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public  void show() {
        frame = new JFrame("注册");
        frame.setContentPane(new Register().registerFrame);
        frame.pack();
        Window.tocenter(frame);
        frame.setVisible(true);
    }
    public void close(){
        frame.dispose();
    }
}
