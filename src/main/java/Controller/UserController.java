package Controller;

import Model.User;
import Utils.Database.Result;

public interface UserController {
    public Result login(String username, String password);
    public Result register(String username, String password,String email);
    public Result updateUserinfo(User oldUser, User newUser);
}
