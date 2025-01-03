package Utils.Database;

import java.sql.*;

public class Connections {
    private static Connection connection;
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");    //1.加载驱动
        } catch (ClassNotFoundException e) {
            System.out.println("▲驱动加载失败！！！");
            e.printStackTrace();
        }
    }
    /**通用连接数据库函数
     *
     * @return {@link Connections}
     */
    public static Connection getConnection() {
        String url = Config.getUrl();
        String user=Config.getUsername();
        String pass=Config.getPassword();
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(url,user,pass);    //2.得到连接
            }
            catch (SQLException e) {
                System.out.println("▲SQLException"+e.getMessage());
                e.printStackTrace();
            }
        }

        return connection;
    }
    public static Result close() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                return new Result(true);
            } catch (SQLException e) {
                return new Result(false, e.getMessage());
            }
        }
        return new Result(false,"断开数据库连接失败");
    }
    /**
     * 通用数据库关闭函数
     *
     * @param rs         rs
     * @param statement  声明
     * @param connection 连接
     */
//    public  static void close(ResultSet rs , Statement statement, Connections connection){
//
//        try {
//            if (rs!=null) {
//                rs.close();
//            }
//            if (statement!=null){
//                statement.close();
//            }
//            if (connection!=null){
//                connection.close(rs,statement,connection);
//            }
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
}
