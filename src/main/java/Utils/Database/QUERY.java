package Utils.Database;

import java.sql.*;

public class QUERY<T> implements OperationStrategy {
    private final String querysql;
    private final Object[] params;
    private final ResultSetHandler<T> handler;

    public QUERY(String querysql, Object[] params, ResultSetHandler<T> handler) {
        this.querysql = querysql;
        this.params = params;
        this.handler = handler;
    }

    @Override
    public Result execute(Connection connection) throws SQLException {
        try {
            PreparedStatement pstmt = connection.prepareStatement(querysql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }
            try {
                ResultSet rs = pstmt.executeQuery();
                T result = handler.handle(rs);
                return new Result(true,"查询成功！",result);
            }catch (SQLException e){
                return new Result(false,e.getMessage());
            }
        }catch (SQLException e){
            return new Result(false, e.getMessage());
        }
    }
}


