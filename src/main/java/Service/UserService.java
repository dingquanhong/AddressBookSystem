package Service;

import Controller.UserController;
import Mapper.UserMapper;
import Model.User;
import Utils.Database.Result;

public class UserService implements UserController {

    @Override
    public Result login(String username, String password) {
        UserMapper mapper = new UserMapper();
        Result result = mapper.findUserByEmail(username);
        if (result.getStatus()){
            User user = (User) result.getData();
            if (user==null){
                return  new Result(false,"用户不存在");
            }
            if (user.getPassword().equals(password)){
                return new Result(true,"登录成功!",user);
            }else {
                return new Result(false,"账号密码错误！");
            }
        }else {
            return new Result(false,result.getMessage());
        }
    }

    @Override
    public Result register(String username, String password, String email) {
        UserMapper userMapper = new UserMapper();
        User u = (User) userMapper.findUserByEmail(email).getData();
        if (u==null) {
            User user = new User(username,password,email);
            return userMapper.addUser(user);
        }else{
            return new Result(false,"用户已注册!");
        }
    }

    @Override
    public Result updateUserinfo(User olduser, User newuser) {
        UserMapper mapper = new UserMapper();
        if (!olduser.getEmail().equals(newuser.getEmail())&&mapper.findUserByEmail(newuser.getEmail()).getData()!=null) {
            return new Result(false,"邮箱已被绑定");
        }else {
            Result result = mapper.updateUserInfo(newuser.getId(),newuser.getUsername(),newuser.getPassword(),newuser.getEmail());
            if (result.getStatus()){
                return new Result(true);
            }else {
                return new Result(false,result.getMessage());
            }
        }
    }

}
