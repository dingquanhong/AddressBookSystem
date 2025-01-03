package Utils.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DELETE implements OperationStrategy{
    private final String deletesql;
    private final Object[] params;

    public DELETE(String deletesql, Object[] params) {
        this.deletesql = deletesql;
        this.params = params;
    }

    @Override
    public Result execute(Connection connection) throws SQLException {
        try {
            PreparedStatement pstmt = connection.prepareStatement(deletesql);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            int affected = pstmt.executeUpdate();
            if (affected == 0){
                return new Result(false, deletesql+" failed");
            }
            return new Result(true, deletesql+" successful");
        }catch (SQLException e){
            return new Result(false, e.getMessage());
        }
    }
}
