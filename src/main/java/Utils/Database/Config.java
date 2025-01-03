package Utils.Database;

public class Config {
    static String url = "jdbc:mysql://localhost:3306/address_book";
    static String username="root";
    static String password="2003.3.29";

    public static String getUrl() {
        return url;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }
}
