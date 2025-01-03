package Controller;

import Model.Contact;
import Model.Group;
import Model.User;
import Utils.Database.Result;

public interface GroupController {
    public Result getUserGroupsByUserID(int userID);
    public Result addUserGroup(int userID, String groupName);

    public Result updateUserGroup( Group group);

    Result deleteUserGroup(Group chosenGroup,Group defaultGroup);


    Result getGroupByID(int groupId);

    Result getUserGroupByName(String groupname,User user);
}
