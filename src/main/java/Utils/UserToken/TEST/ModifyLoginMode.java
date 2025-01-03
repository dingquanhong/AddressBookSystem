package Utils.UserToken.TEST;

import Utils.UserToken.UserManager;
import Utils.UserToken.UserMemo;
import Utils.UserToken.UserToken;

public class ModifyLoginMode {
    public static void main(String[] args) {
        UserManager manager = new UserManager();
        UserMemo memo = (UserMemo) manager.load().getData();
        int newmode = 0;
        if (memo.getMode()==0){
            newmode = 1;
        }
        UserToken token = new UserToken(newmode,memo.getUsername(),memo.getPassword(),memo.getTime());
        UserMemo newmemo = token.createMemo();
        manager.save(newmemo);
    }
}
