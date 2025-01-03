package Utils.UserToken;

import java.sql.Timestamp;

public class UserToken {
    int mode;
    private String username;
    private String password;
    private Timestamp time;
    public UserMemo createMemo() {
        UserMemo userMemo = new UserMemo();
        userMemo.setMode(mode);
        userMemo.setUsername(username);
        userMemo.setPassword(password);
        userMemo.setTime(time);
        return userMemo;
    }
    public void restoreFromMemo(UserMemo userMemo) {
        mode = userMemo.getMode();
        username = userMemo.getUsername();
        password = userMemo.getPassword();
        time = userMemo.getTime();
    }

    public UserToken(int mode, String username, String password) {
        this.mode = mode;
        this.username = username;
        this.password = password;
        this.time = new Timestamp(System.currentTimeMillis());
    }

    public UserToken(int mode, String username, String password, Timestamp time) {
        this.mode = mode;
        this.username = username;
        this.password = password;
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Timestamp getTime() {
        return time;
    }

    public int getMode() {
        return mode;
    }
}
