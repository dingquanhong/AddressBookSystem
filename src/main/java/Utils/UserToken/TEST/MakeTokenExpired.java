package Utils.UserToken.TEST;

import Utils.UserToken.UserManager;
import Utils.UserToken.UserMemo;
import Utils.UserToken.UserToken;

import java.sql.Timestamp;
import java.util.Date;

public class MakeTokenExpired {
    public static void main(String[] args) {
        Date date = new Date(2025,1,1);
        Timestamp timestamp = new Timestamp(date.getTime());
        UserManager manager = new UserManager();
        UserToken token = new UserToken(1,"2772670785@qq.com","2003.3.29",timestamp);
        UserMemo memo = token.createMemo();
        manager.save(memo);
    }
}
