package View;

import Model.User;
import Service.UserService;
import Utils.Database.Result;
import Utils.View.Window;
import Utils.UserSecssion.UserSecssion;


import javax.swing.*;
import java.awt.event.*;

public class EditUserinfo extends JDialog {
    private User user;

    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel editFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JPanel EditUserinfoDailog;
    private static JFrame frame;
    private static JLabel username;
    private static JLabel email;
    public EditUserinfo() {
        setContentPane(EditUserinfoDailog);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        user = UserSecssion.getSecssion().getUser();
        if (user != null) {
            usernameField.setText(user.getUsername());
            passwordField.setText(user.getPassword());
            emailField.setText(user.getEmail());
        }else {
            JOptionPane.showMessageDialog(this, "用户信息提取错误");
        }
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // 点击 X 时调用 onCancel()
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // 遇到 ESCAPE 时调用 onCancel()
        EditUserinfoDailog.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        UserService service = new UserService();
        int id = user.getId();
        String name = usernameField.getText();
        String pass = String.valueOf(passwordField.getPassword());
        String email = emailField.getText();
        User newUser = new User(id, name, pass, email);
        Result result = service.updateUserinfo(user,newUser);
        if (result.getStatus()){
            UserSecssion secssion = UserSecssion.getSecssion();
            secssion.setUser(newUser);
            JOptionPane.showMessageDialog(editFrame,"用户信息更新成功!");
            close();
        }else {
            JOptionPane.showMessageDialog(editFrame,result.getMessage());
        }

    }

    private void onCancel() {

        close();
    }
    private void close(){
        User user = UserSecssion.getSecssion().getUser();
        username.setText(user.getUsername());
        email.setText(user.getEmail());
        frame.dispose();
    }
    public void show(JLabel usernamelable, JLabel emailable) {
        username=usernamelable;
        email=emailable;
        frame = new JFrame();
        frame.setContentPane(new EditUserinfo().EditUserinfoDailog);
        frame.pack();
        Window.tocenter(frame);
        frame.setVisible(true);

    }
}
