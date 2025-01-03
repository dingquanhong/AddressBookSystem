package Utils.DeleteUserGroup;

import Mapper.GroupMapper;
import Model.Group;
import Service.GroupService;
import Utils.Database.Result;

public class DeleteUserGroup implements Handler{
    private Handler handler;
    @Override
    public Result handleRequest(Group group, Group defaultGroup, Result prevResult) {
        if (prevResult!=null&&prevResult.getStatus()){
            GroupMapper mapper = new GroupMapper();
            Result result = mapper.deleteUserGroup(group);
            if (handler!=null){
                handler.handleRequest(group,defaultGroup,result);
            }
            return result;
        }
        return new Result(false,"delete责任链配置错误");
    }

    @Override
    public void setNextHandler(Handler handler) {
        this.handler = handler;
    }
}
