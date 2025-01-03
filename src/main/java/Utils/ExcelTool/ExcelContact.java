package Utils.ExcelTool;

import Model.Contact;

public class ExcelContact extends Contact {
    private String groupName;
    public ExcelContact(int id, int userId, int groupId, String name, int sex, String phone, String email, String workunit, String address, String notes) {
        super(id, userId, groupId, name, sex, phone, email, workunit, address, notes);
    }
    public ExcelContact(int id, int userId, int groupId, String name, int sex, String phone, String email, String workunit, String address, String notes,String groupName) {
        super(id, userId, groupId, name, sex, phone, email, workunit, address, notes);
        this.groupName = groupName;
    }
    public String getGroupName() {
        return groupName;
    }
}
