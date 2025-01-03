package Utils.UserSecssion;

import Model.User;

import java.sql.Timestamp;

public class UserSecssion {
    private static UserSecssion secssion;
    private User user;
    public static UserSecssion getSecssion() {
        if (UserSecssion.secssion == null) {
            secssion = new UserSecssion();
        }
        return secssion;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public UserSecssion() {
    }
}
