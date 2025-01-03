package Utils.DeleteUserGroup;

import Mapper.ContactMapper;
import Model.Contact;
import Model.Group;
import Service.ContactService;
import Utils.Database.Result;

import java.util.ArrayList;

public class MoveToDefautGroup implements Handler{
    private Handler handler;
    @Override
    public Result handleRequest(Group group, Group defaultGroup, Result prevResult) {
        if (prevResult!=null&&prevResult.getStatus()){
            ContactService service = new ContactService();
            ArrayList<Contact> contacts = (ArrayList<Contact>) prevResult.getData();
            for (Contact contact:contacts){
                Result moveResult = service.moveContact(contact,defaultGroup);
                if (!moveResult.getStatus()){
                    return moveResult;
                }
            }
            Result result=new Result(true);
            if (handler!=null){
                handler.handleRequest(group,defaultGroup,result);
            }
            return result;
        }
        return new Result(false,"move责任链配置错误");
    }

    @Override
    public void setNextHandler(Handler handler) {
        this.handler=handler;
    }
}
