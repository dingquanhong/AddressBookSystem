package Utils.Database;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseContext {
    private final Connection connection;

    public DatabaseContext(Connection connection) {
        this.connection = connection;
    }

    public Result peformOperation(OperationStrategy strategy) {
        try {
            return strategy.execute(connection);
        }catch (SQLException e){
            return new Result(false, e.getMessage());
        }
    }
}
