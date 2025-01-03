package Utils.Database;

public class Result {
    private Boolean status;
    private String message;
    private Object data = null;
    public Result(Boolean status, String message) {
        this.status = status;
        this.message = message;
        if (!status){
            System.out.println("错误:"+message);
        }
    }

    public Result(Boolean status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
        if (!status){
            System.out.println("错误:"+message);
        }
    }

    public Result(Boolean status) {
        this.status = status;
        if (!status){
            System.out.println("错误:"+message);
        }
    }

    public Boolean getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
    public Object getData() {
        return data;
    }
}
