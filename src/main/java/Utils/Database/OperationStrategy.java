package Utils.Database;

import java.sql.Connection;
import java.sql.SQLException;

public interface OperationStrategy {
    public Result execute(Connection connection) throws SQLException;
}
