package Mapper;

import Model.User;
import Utils.Database.*;

import java.sql.*;

public class UserMapper {
    private static Connection coon = Connections.getConnection();
    private static DatabaseContext context = new DatabaseContext(coon);
    public Result findUserByEmail(String useremail) {
        String sql = "select * from user where email = ?";
        Object[] params = {useremail};
        OperationStrategy strategy = new QUERY(sql,params, rs -> {
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                Timestamp time = rs.getTimestamp("createtime");
                User user = new User(id, username, password, email, time);
                return user;
            }
            return null;
        });
        return context.peformOperation(strategy);
    }
    public Result addUser(User user) {
        String sql = "INSERT INTO user VALUES (null,?,?,?,?)";
        Object[] params = new Object[]{
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                new Timestamp(System.currentTimeMillis())
        };
        OperationStrategy strategy = new INSERT(sql,params);
        return context.peformOperation(strategy);
    }

    public Result updateUserInfo(int id,String username, String password, String email) {
        String sql = "update user set username = ?, password = ?, email = ? where id = ?";
        Object[] params = {username,password,email,id};
        OperationStrategy strategy = new UPDATE(sql,params);
        return context.peformOperation(strategy);
    }
}
