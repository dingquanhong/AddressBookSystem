package Utils.View.TableObserver;

import Model.Contact;
import Service.ContactService;
import Utils.Database.Result;
import View.Home;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ContactTableRender {
    public static void refresh(JTable table){
        TableRender render = new TableRender(table);
        ContactService service = new ContactService();
        if (Home.getChosengroup()==null){
            return;
        }
        Result result = service.getContactsByGroupID(Home.getChosengroup().getId());
        String[] tabletitle = {"姓名","性别","电话","邮箱","工作单位","住址"};
        DefaultTableModel model = new DefaultTableModel(tabletitle,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ArrayList<Contact> contacts = (ArrayList<Contact>) result.getData();
        Home.setContacts(contacts);
        for (Contact contact : contacts) {
            String sex = "男";
            if (contact.getSex()==0){
                sex="女";
            }
            Object[] row = {
                    contact.getName(),
                    sex,
                    contact.getPhone(),
                    contact.getEmail(),
                    contact.getWorkunit(),
                    contact.getAddress(),
            };
            model.addRow(row);
        }
        render.refreshTable(model);
    }
}
