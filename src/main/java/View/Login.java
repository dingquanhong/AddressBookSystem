package View;

import Model.User;
import Service.UserService;
import Utils.Database.Result;
import Utils.View.Window;
import Utils.UserSecssion.UserSecssion;
import Utils.UserToken.UserManager;
import Utils.UserToken.RememberDay;
import Utils.UserToken.UserMemo;
import Utils.UserToken.UserToken;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {
    private JPanel LoginFrame;
    private JPanel loginform;
    private JLabel title;
    private JTextField username;
    private JPasswordField password;
    private JButton loginBtn;
    private JButton registerBtn;
    private JCheckBox rememberpassword;
    private JCheckBox automaticlogin;
    private JLabel imgbox;
    static JFrame frame;

    public Login() {
        ImageIcon icon = new ImageIcon("src/main/resources/img/login/icon.png");
        imgbox.setIcon(icon);
        automaticlogin.setText(RememberDay.num+"天内自动登录");
        UserManager manager = new UserManager();
        Result userinfo = manager.load();
        if (userinfo.getStatus()){
            UserMemo memo = (UserMemo) userinfo.getData();
            String memoUsername = memo.getUsername();
            String memoPassword = memo.getPassword();
            username.setText(memoUsername);
            password.setText(memoPassword);
            if (memo.getMode()==1){
                automaticlogin.setSelected(true);
            }
            rememberpassword.setSelected(true);
            if (memo.getMode()==-1){
                rememberpassword.setSelected(false);
            }
        }
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String uname = username.getText();
                String pass = password.getText();
                UserService userService = new UserService();
                Result result = userService.login(uname, pass);
                if (result.getStatus()) {
                    UserMemo memo = null;
                    UserToken token = null;
                    if (rememberpassword.isSelected()) {
                        token = new UserToken(0,uname,pass);
                        memo = token.createMemo();
                        manager.save(memo);
                    }
                    if (automaticlogin.isSelected()) {
                        token = new UserToken(1,uname,pass);
                        memo = token.createMemo();
                        manager.save(memo);
                    }
                    if (!rememberpassword.isSelected()){
                        token = new UserToken(-1,"","");
                        memo = token.createMemo();
                        manager.save(memo);
                    }
                    User user = (User) result.getData();
                    UserSecssion.getSecssion().setUser(user);
                    close();
                    Home home = new Home();
                    home.show();
                }else{
                    JOptionPane.showMessageDialog(null,result.getMessage());
                }
            }
        });
        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Register register = new Register();
                register.show();
            }
        });
        automaticlogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (automaticlogin.isSelected()) {
                    rememberpassword.setSelected(true);
                }else {
                    rememberpassword.setSelected(false);
                }
            }
        });
    }

    public void show() {
        ImageIcon icon = new ImageIcon("src/main/resources/img/logo.png");
        frame = new JFrame("登录");
        frame.setIconImage(icon.getImage());
        frame.setContentPane(new Login().LoginFrame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        Window.tocenter(frame);
        frame.setVisible(true);

    }
    public void close(){
        frame.dispose();
    }

    public static JFrame getFrame() {
        return frame;
    }
}
