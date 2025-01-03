package Utils.DeleteUserGroup;

import Model.Group;
import Utils.Database.Result;

public interface Handler {
    public abstract Result handleRequest(Group group, Group defaultGroup,Result prevResult);
    public abstract void setNextHandler(Handler handler);
}
