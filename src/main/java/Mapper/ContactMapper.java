package Mapper;

import Model.Contact;
import Model.Group;
import Model.User;
import Utils.Database.*;

import java.sql.Connection;
import java.util.ArrayList;

public class ContactMapper {
    private static Connection conn = Connections.getConnection();
    private static DatabaseContext context = new DatabaseContext(conn);
    public Result getContactsByGroupID(int groupID) {
        String sql = "select * from contact where groupid=?";
        Object[] params = {groupID};
        OperationStrategy strategy = new QUERY(sql,params,rs->{
            ArrayList<Contact> contacts = new ArrayList<Contact>();
            while (rs.next()) {
               int id=rs.getInt("id");
               int userId=rs.getInt("userid");
               int groupId=rs.getInt("groupid");
               String name=rs.getString("name");
               int sex=rs.getInt("sex");
               String phone=rs.getString("phone");
               String email=rs.getString("email");
               String workunit=rs.getString("workunit");
               String address=rs.getString("address");
               String notes=rs.getString("notes");
               Contact contact = new Contact(id,userId,groupId,name,sex,phone,email,workunit,address,notes);
               contacts.add(contact);
           }
           return contacts;
        });
        return context.peformOperation(strategy);
    }

    public Result addContact(Contact contact) {
        String sql = "INSERT INTO contact VALUES(?,?,?,?,?,?,?,?,?,?)";
        Object[] params = {
                null,
                contact.getUserId(),
                contact.getGroupId(),
                contact.getName(),
                contact.getSex(),
                contact.getPhone(),
                contact.getEmail(),
                contact.getWorkunit(),
                contact.getAddress(),
                contact.getNotes()
        };
        OperationStrategy strategy = new INSERT(sql,params);
        return context.peformOperation(strategy);
    }

    public Result updateContact(Contact contact) {
        String sql = "UPDATE contact SET groupid=?,name=?,sex=?,phone=?,email=?,workunit=?,address=?,notes=? WHERE id=?";
        Object[] params = {
                contact.getGroupId(),
                contact.getName(),
                contact.getSex(),
                contact.getPhone(),
                contact.getEmail(),
                contact.getWorkunit(),
                contact.getAddress(),
                contact.getNotes(),
                contact.getId()
        };
        OperationStrategy strategy = new UPDATE(sql,params);
        return context.peformOperation(strategy);
    }

    public Result deleteContact(Contact contact) {
        String sql = "DELETE FROM contact WHERE id=?";
        Object[] params = {contact.getId()};
        OperationStrategy strategy = new DELETE(sql,params);
        return context.peformOperation(strategy);
    }

    public Result querryContacts(Group group, String name, String phone, int sex,String email, String address, String workunit) {
        StringBuilder sql = new StringBuilder("SELECT * FROM contact WHERE 1=1");
        ArrayList<Object> params = new ArrayList<>();

        if (group.getId() != -1) {
            sql.append(" AND groupid = ?");
            params.add(group.getId());
        }

        if (name != null && !name.isEmpty()) {
            sql.append(" AND name LIKE ?");
            params.add("%" + name + "%");
        }

        if (phone != null && !phone.isEmpty()) {
            sql.append(" AND phone LIKE ?");
            params.add("%" + phone + "%");
        }

        if (sex != -1) {
            sql.append(" AND sex = ?");
            params.add(sex);
        }
        if (email!=null&&!email.isEmpty()){
            sql.append(" AND email LIKE ?");
            params.add("%"+email+"%");
        }

        if (address != null && !address.isEmpty()) {
            sql.append(" AND address LIKE ?");
            params.add("%" + address + "%");
        }

        if (workunit != null && !workunit.isEmpty()) {
            sql.append(" AND workunit LIKE ?");
            params.add("%" + workunit + "%");
        }

        OperationStrategy strategy = new QUERY(sql.toString(), params.toArray(), rs -> {
            ArrayList<Contact> contacts = new ArrayList<>();
            while (rs.next()) {
                Contact contact = new Contact(
                        rs.getInt("id"),
                        rs.getInt("userid"),
                        rs.getInt("groupid"),
                        rs.getString("name"),
                        rs.getInt("sex"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("workunit"),
                        rs.getString("address"),
                        rs.getString("notes")
                );
                contacts.add(contact);
            }
            return contacts;
        });

        return context.peformOperation(strategy);
    }

    public Result getUserContacts(User user) {
        String sql = "SELECT * FROM contact WHERE userid = ?";
        Object[] params={user.getId()};
        return context.peformOperation(new QUERY(sql,params,rs->{
            ArrayList<Contact> contacts = new ArrayList<>();
            while (rs.next()){
                Contact contact = new Contact(
                        rs.getInt("id"),
                        rs.getInt("userid"),
                        rs.getInt("groupid"),
                        rs.getString("name"),
                        rs.getInt("sex"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("workunit"),
                        rs.getString("address"),
                        rs.getString("notes")
                );
               contacts.add(contact);
            }
            return contacts;
        }));
    }
}
