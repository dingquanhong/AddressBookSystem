package Model;

import lombok.Data;

@Data
public class Contact {
    private int id;
    private int userId;
    private int groupId;
    private String name;
    private int sex;
    private String phone;
    private String email;
    private String workunit;
    private String address;
    private String notes;

    public Contact(int id, int userId, int groupId, String name, int sex, String phone, String email, String workunit, String address, String notes) {
        this.id = id;
        this.userId = userId;
        this.groupId = groupId;
        this.name = name;
        this.sex = sex;
        this.phone = phone;
        this.email = email;
        this.workunit = workunit;
        this.address = address;
        this.notes = notes;
    }

}
