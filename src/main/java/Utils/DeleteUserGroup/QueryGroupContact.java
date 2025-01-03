package Utils.DeleteUserGroup;

import Mapper.ContactMapper;
import Model.Contact;
import Model.Group;
import Service.ContactService;
import Utils.Database.Result;

public class QueryGroupContact implements Handler{
    private Handler handler;
    @Override
    public Result handleRequest(Group group, Group defaultGroup, Result prevResult) {
        ContactService service = new ContactService();
        Result result = service.getContactsByGroupID(group.getId());
        if (handler!=null){
            handler.handleRequest(group,defaultGroup,result);
        }
        return result;
    }

    @Override
    public void setNextHandler(Handler handler) {
        this.handler=handler;
    }
}
