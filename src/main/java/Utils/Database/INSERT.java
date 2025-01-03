package Utils.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class INSERT implements OperationStrategy{
    private final String insertSQL;
    private final Object[] params;

    public INSERT(String insertSQL, Object[] params) {
        this.insertSQL = insertSQL;
        this.params = params;
    }

    @Override
    public Result execute(Connection connection) throws SQLException {
        try {
            PreparedStatement pstmt = connection.prepareStatement(insertSQL);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            pstmt.executeUpdate();
            return new Result(true, insertSQL+" inserted successfully");
        }catch (SQLException e) {
            return new Result(false, e.getMessage());
        }
    }
}
