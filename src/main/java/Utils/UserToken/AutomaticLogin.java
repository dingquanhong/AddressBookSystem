package Utils.UserToken;

import Model.User;
import Service.UserService;
import Utils.Database.Result;
import Utils.UserSecssion.UserSecssion;
import View.Home;
import View.Login;

import javax.swing.*;
import java.sql.Timestamp;

public class AutomaticLogin {
    public static void autologin(){
        UserManager manager = new UserManager();
        Result userinfo = manager.load();
        if (userinfo.getStatus()) {
            UserMemo memo = (UserMemo) userinfo.getData();
            String memoUsername = memo.getUsername();
            String memoPassword = memo.getPassword();
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            if (Math.abs(currentTime.getTime()-memo.getTime().getTime())<RememberDay.num*24*60*60*1000){
                if (memo.getMode() == 1) {
                    UserService userService = new UserService();
                    Result result = userService.login(memoUsername, memoPassword);
                    if (result.getStatus()) {
                        User user = (User) result.getData();
                        UserSecssion.getSecssion().setUser(user);
                        Home home = new Home();
                        home.show();
                        return;
                    } else {
                        JOptionPane.showMessageDialog(null, result.getMessage());
                    }
                }
            }else {
                JOptionPane.showMessageDialog(null,"用户信息已过期,请重新登录!");
            }

        }
        Login login = new Login();
        login.show();
    }
}
