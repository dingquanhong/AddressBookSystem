package Mapper;

import Model.Contact;
import Model.Group;
import Model.User;
import Utils.Database.*;

import java.sql.Connection;
import java.util.ArrayList;

public class GroupMapper  {
    private static Connection conn = Connections.getConnection();
    private static DatabaseContext context = new DatabaseContext(conn);
    public Result getUserGroupsByUserID(int userID) {
        String sql = "select * from `groups` where userid = ?";
        Object[] params = new Object[]{userID};
        OperationStrategy strategy = new QUERY(sql,params,rs-> {
            ArrayList<Group> groups = new ArrayList<>();
            while (rs.next()) {
                int id;
                int userId;
                String groupName;
                Boolean isDefault;
                id = rs.getInt("id");
                userId = rs.getInt("userid");
                groupName = rs.getString("groupname");
                isDefault = rs.getBoolean("isdefault");
                Group group = new Group(id, userId, groupName, isDefault);
                groups.add(group);
            }
            return groups;
        });
        return context.peformOperation(strategy);
    }

    public Result addUserGroup(int userID, String groupName) {
        String sql = "INSERT INTO `groups` VALUES (null,?,?,?)";
        Object[] params = new Object[]{userID, groupName, false};
        OperationStrategy strategy = new INSERT(sql,params);
        return context.peformOperation(strategy);
    }

    public Result updateUserGroup(Group group) {
        String sql = "UPDATE `groups` SET  groupname = ? WHERE id = ?";
        Object[] params = new Object[]{group.getGroupName(),group.getId()};
        OperationStrategy strategy = new UPDATE(sql,params);
        return context.peformOperation(strategy);
    }

    public Result deleteUserGroup(Group group) {
        String sql = "DELETE FROM `groups` WHERE id = ?";
        Object[] params = new Object[]{group.getId()};
        OperationStrategy strategy = new DELETE(sql,params);
        return context.peformOperation(strategy);
    }

    public Result getGroupByID(int groupId) {
        String sql = "select * from `groups` where id = ?";
        Object[] params = new Object[]{groupId};
        OperationStrategy strategy = new QUERY(sql,params,rs->{
            Group group = null;
            if (rs.next()){
                int id = rs.getInt("id");
                int userId = rs.getInt("userid");
                String groupName = rs.getString("groupname");
                Boolean isDefault = rs.getBoolean("isdefault");
                group = new Group(id,userId,groupName,isDefault);
            }
            return group;
        });
        return context.peformOperation(strategy);
    }


    public Result getUserGroupByName(String groupname, User user) {
        if (user!=null){
            String sql = "select * from `groups` where groupname = ? and userid = ?";
            Object[] params = new Object[]{groupname,user.getId()};
            OperationStrategy strategy = new QUERY(sql,params,rs->{
               while (rs.next()){
                   int id = rs.getInt("id");
                   int userId = rs.getInt("userid");
                   String groupName = rs.getString("groupname");
                   Boolean isDefault = rs.getBoolean("isdefault");
                   return new Group(id,userId,groupName,isDefault);
               }
               return null;
            });
            return context.peformOperation(strategy);
        }else {
            return new Result(false,"查询联系人分组错误");
        }

    }
}
