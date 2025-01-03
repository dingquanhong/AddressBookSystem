package Service;

import Controller.GroupController;
import Mapper.GroupMapper;
import Model.Contact;
import Model.Group;
import Model.User;
import Utils.Database.Result;
import Utils.DeleteUserGroup.DeleteUserGroup;
import Utils.DeleteUserGroup.MoveToDefautGroup;
import Utils.DeleteUserGroup.QueryGroupContact;
import Utils.UserSecssion.UserSecssion;

import java.sql.Connection;

public class GroupService implements GroupController {
    private static GroupMapper mapper = new GroupMapper();
    @Override
    public Result getUserGroupsByUserID(int userID) {
        Result result = mapper.getUserGroupsByUserID(userID);
        if (result!=null) {
            if (result.getStatus()) {
                return new Result(true, "Success",result.getData());
            }else {
                return new Result(false, result.getMessage());
            }
        }else {
            return new Result(false, "查询用户分组目录为空");
        }

    }

    @Override
    public Result addUserGroup(int userID, String groupName) {
        User user = UserSecssion.getSecssion().getUser();
        //查询是否存在重名分组
        Result result = mapper.getUserGroupByName(groupName,user);
        if (result.getData()==null){
            result = mapper.addUserGroup(userID, groupName);
            if (result!=null) {
                if (result.getStatus()) {

                    return new Result(true, "Success",result.getData());
                }else {
                    return new Result(false, result.getMessage());
                }
            }else {
                return new Result(false,"添加分组错误！");
            }
        }else {
            return new Result(false,"分组名不能重复");
        }
    }

    @Override
    public Result updateUserGroup(Group group) {
        Result result = mapper.updateUserGroup(group);
        return result;
    }

    @Override
    public Result deleteUserGroup(Group chosenGroup,Group defaultGroup) {
        QueryGroupContact queryGroupContact = new QueryGroupContact();
        MoveToDefautGroup moveToDefautGroup = new MoveToDefautGroup();
        DeleteUserGroup deleteUserGroup = new DeleteUserGroup();
        queryGroupContact.setNextHandler(moveToDefautGroup);
        moveToDefautGroup.setNextHandler(deleteUserGroup);
        return queryGroupContact.handleRequest(chosenGroup,defaultGroup,null);
    }

    @Override
    public Result getGroupByID(int groupId) {
        return mapper.getGroupByID(groupId);
    }

    @Override
    public Result getUserGroupByName(String groupname,User user) {
        return mapper.getUserGroupByName(groupname,user);
    }


}
