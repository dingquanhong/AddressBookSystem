package Utils.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UPDATE implements OperationStrategy{
    private final String updatasql;
    private final Object[] params;

    public UPDATE(String updatasql, Object[] params) {
        this.updatasql = updatasql;
        this.params = params;
    }

    @Override
    public Result execute(Connection connection) throws SQLException {
        try {
            PreparedStatement pstmt = connection.prepareStatement(updatasql);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            int affected =pstmt.executeUpdate();
            if (affected == 0) {
                return new Result(false, "No rows affected");
            }
            return new Result(true,affected+"条数据"+"更新成功!");
        }catch (SQLException e){
            return new Result(false, e.getMessage());
        }
    }
}
