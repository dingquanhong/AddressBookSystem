package Model;

import lombok.Data;

import java.sql.Timestamp;
@Data
public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private Timestamp createtime;

    public User( String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(int id, String username, String password, String email, Timestamp createtime) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.createtime = createtime;
    }

    public User(int id, String name, String pass, String email) {
        this.id = id;
        this.username = name;
        this.password = pass;
        this.email = email;
    }
}
