package Utils.View.GroupObserver;

import Controller.GroupController;
import Model.Contact;
import Model.Group;
import Model.User;
import Service.ContactService;
import Service.GroupService;
import Utils.Database.Result;
import Utils.UserSecssion.UserSecssion;
import View.GroupDialog;
import View.Home;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GroupListRender implements OnGroupChangedListener{
    private static JList groupList;
    private static JScrollPane groupListPane;
    private static JComboBox gnameSearch;
    private static JButton addContactBtn;
    private static JTable contactTable;
    private static Home home;
    public GroupListRender(Home home, JList groupList, JScrollPane groupListPane, JComboBox gnameSearch, JButton addContactBtn, JTable contactTable) {
        this.home=home;
        this.groupList = groupList;
        this.groupListPane = groupListPane;
        this.gnameSearch = gnameSearch;
        this.addContactBtn = addContactBtn;
        this.contactTable = contactTable;
    }


    public static ArrayList<Group> getGroupList(){
        User user = UserSecssion.getSecssion().getUser();
        GroupController service = new GroupService();
        Result result = service.getUserGroupsByUserID(user.getId());
        ArrayList<Group> groups = (ArrayList<Group>) result.getData();
        DefaultListModel<Group> model = new DefaultListModel<>();
        gnameSearch.removeAllItems();
        gnameSearch.addItem(new Group(-1,-1,"全部分组",false));
        for (Group group : groups) {
            model.addElement(group);
            gnameSearch.addItem(group);
        }
        groupList = new JList(model) {
            @Override
            public int locationToIndex(Point location) {
                int index = super.locationToIndex(location);
                if (index != -1 && !getCellBounds(index, index).contains(location)) {
                    return -1;
                }
                else {
                    return index;
                }
            }

        };
        groupList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Group chosenGroup = (Group) groupList.getSelectedValue();
                home.setChosengroup(chosenGroup);
                if (chosenGroup == null) {
                    addContactBtn.setEnabled(false);
                    return;
                }
                addContactBtn.setEnabled(true);
                ContactService service = new ContactService();
                Result result = service.getContactsByGroupID(chosenGroup.getId());
                if (result.getStatus()){
                    String[] tabletitle = {"姓名","性别","电话","邮箱","工作单位","住址"};
                    DefaultTableModel model = new DefaultTableModel(tabletitle,0){
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }
                    };
                    ArrayList<Contact> contacts = (ArrayList<Contact>) result.getData();
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
                    home.setContacts(contacts);
                    contactTable.setModel(model);
                }else {
                    JOptionPane.showMessageDialog(null,result.getMessage());
                }
            }
        });

        groupList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                JPopupMenu popupMenu = new JPopupMenu();
                int index = groupList.locationToIndex(e.getPoint());
                Group chosenGroup = (Group) groupList.getSelectedValue();
                if (e.getClickCount()==2){
                    if (index!=-1){
                        GroupListRender glist = new GroupListRender(home,groupList, groupListPane, gnameSearch, addContactBtn, contactTable);
                        GroupDialog groupDialog = new GroupDialog(glist);
                        groupDialog.show(chosenGroup);
                        return;
                    }
                }
                if (e.isPopupTrigger()){
                    if (index!=-1){

                        if (chosenGroup==null){
                            return;
                        }
                        groupList.setSelectedIndex(index);
                        JMenuItem renameItem = new JMenuItem("重命名");
                        renameItem.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                GroupListRender glist = new GroupListRender(home,groupList, groupListPane, gnameSearch, addContactBtn, contactTable);
                                GroupDialog groupDialog = new GroupDialog(glist);
                                groupDialog.show(chosenGroup);
                            }
                        });
                        JMenuItem deleteItem = new JMenuItem("删除");
                        deleteItem.addActionListener(new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                Group defaultGroup = new Group();
                                for (Group group : groups){
                                    if (group.getIsDefault()){
                                        defaultGroup=group;
                                        break;
                                    }
                                }
                                GroupService service = new GroupService();
                                Result result = service.deleteUserGroup(chosenGroup,defaultGroup);
                                if (result.getStatus()){
                                    JOptionPane.showMessageDialog(null,"删除成功");
                                    getGroupList();
                                }else {
                                    JOptionPane.showMessageDialog(null,result.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        });
                        if (chosenGroup.getIsDefault()){
                            deleteItem.setEnabled(false);
                        }
                        popupMenu.add(deleteItem);
                        popupMenu.add(renameItem);
                    }else {
                        groupList.clearSelection();
                        JMenuItem refreshItem = new JMenuItem("刷新");
                        refreshItem.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                getGroupList();
                            }
                        });
                        popupMenu.add(refreshItem);
                    }
                }
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        });
        groupList.setFixedCellHeight(60);
        groupListPane.setViewportView(groupList);

        return groups;
    }

    @Override
    public void onGroupChanged() {
        getGroupList();
    }
}
