package Controller;

import Model.Contact;
import Model.Group;
import Model.User;
import Utils.Database.Result;

import java.util.ArrayList;

public interface ContactController {
    Result getContactsByGroupID(int groupID);

    Result addContact(Contact contact);

    Result updateContact(Contact contact);

    Result deleteContact(Contact contact);

    Result moveContact(Contact contact, Group group);


    Result querryContacts(Group group, String name, String phone, int sex, String email,String address, String workunit);

    Result getUserContacts(User user);
}
