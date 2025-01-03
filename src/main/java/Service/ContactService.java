package Service;

import Controller.ContactController;
import Mapper.ContactMapper;
import Model.Contact;
import Model.Group;
import Model.User;
import Utils.Database.Result;

import java.util.ArrayList;

public class ContactService implements ContactController {
    private static ContactMapper mapper = new ContactMapper();
    @Override
    public Result getContactsByGroupID(int groupID) {
        return mapper.getContactsByGroupID(groupID);
    }

    @Override
    public Result addContact(Contact contact) {
        return mapper.addContact(contact);
    }

    @Override
    public Result updateContact(Contact contact) {
        return mapper.updateContact(contact);
    }

    @Override
    public Result deleteContact(Contact contact) {
        return mapper.deleteContact(contact);
    }

    @Override
    public Result moveContact(Contact contact, Group group) {
        Result result = mapper.updateContact(
                new Contact(
                        contact.getId(),
                        contact.getUserId(),
                        group.getId(),
                        contact.getName(),
                        contact.getSex(),
                        contact.getPhone(),
                        contact.getEmail(),
                        contact.getWorkunit(),
                        contact.getAddress(),
                        contact.getNotes()
                )
        );
        return result;
    }

    @Override
    public Result querryContacts(Group group, String name, String phone, int sex, String email,String address, String workunit) {
        return mapper.querryContacts(group,name,phone,sex,email,address,workunit);
    }

    @Override
    public Result getUserContacts(User user) {
        return mapper.getUserContacts(user);
    }


}
