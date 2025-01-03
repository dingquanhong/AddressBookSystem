package Model;

import lombok.Data;

@Data
public class Group {
    private int id;
    private int userId;
    private String groupName;
    private Boolean isDefault;

    public Group() {

    }

    @Override
    public String toString() {
        return groupName;
    }

    public Group(int id, int userId, String groupName, Boolean isDefault) {
        this.id = id;
        this.userId = userId;
        this.groupName = groupName;
        this.isDefault = isDefault;
    }
}
