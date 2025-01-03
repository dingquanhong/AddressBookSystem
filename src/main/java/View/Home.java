package View;

import Model.Contact;
import Model.Group;
import Model.User;
import Service.ContactService;
import Service.GroupService;
import Utils.Database.Result;
import Utils.View.GroupObserver.GroupListRender;
import Utils.View.TableObserver.ContactTableRender;
import Utils.View.TableObserver.TableRender;
import Utils.View.Theme.ThemeList;
import Utils.View.Theme.ThemeManager;
import Utils.View.Theme.ThemeToken;
import Utils.View.Window;
import Utils.UserSecssion.UserSecssion;
import com.formdev.flatlaf.IntelliJTheme;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ArrayList;

public class Home {
    private JButton logoutBtn;
    private JButton editBtn;
    private JLabel avatar;
    private JLabel username;
    private JLabel email;
    private JPanel homeFrame;
    private JButton addGroupBtn;
    private JList groupList;
    private JComboBox gnameSearch;
    private JRadioButton s_man;
    private JRadioButton s_woman;
    private JScrollPane groupListPane;
    public JTable contactTable;
    private JButton addContactBtn;
    private JTextField s_username;
    private JTextField s_phone;
    private JButton searchBtn;
    private JTextField s_address;
    private JTextField s_workunit;
    private JTable searchTable;
    private JToolBar toolBar;
    private JButton themeBtn;
    private JButton fileBtn;
    private JButton aboutBtn;
    private JRadioButton allSex;
    private JTextField s_email;
    private JTabbedPane tabbedPane1;
    private JPanel groupPanel;
    private JScrollPane tablePane;
    private JPanel searchInput;
    private JScrollPane searchPane;
    static JFrame frame;
    private static User user;
    private static ArrayList<Contact> contacts;
    private static ArrayList<Contact> data;
    private static Group chosengroup;

    public static Group getChosengroup() {
        return chosengroup;
    }

    public static void setContacts(ArrayList<Contact> contacts) {
        Home.contacts = contacts;
    }

    public static void setChosengroup(Group chosengroup) {
        Home.chosengroup = chosengroup;
    }

    public Home() {
        init();
        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login login = new Login();
                login.show();
                close();
            }
        });


        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditUserinfo editUserinfo = new EditUserinfo();
                editUserinfo.show(username,email);

            }
        });
        addGroupBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addGroup();
            }
        });


        addContactBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addContact();
            }
        });
        contactTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editContact(contactTable,contacts);
                }
            }
        });

        contactTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                //使得点击位置高亮
                int row = contactTable.rowAtPoint(e.getPoint());
                if (row != -1){
                    contactTable.setRowSelectionInterval(row,row);

                    if (e.isPopupTrigger()){
                        JPopupMenu popupMenu = tablePopupMenu(contactTable,contacts);
                        popupMenu.show(contactTable,e.getX(),e.getY());
                    }
                }
            }
        });
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ContactService service  = new ContactService();
                int sex=-1;
                if (allSex.isSelected()){
                    sex=-1;
                }
                if (s_man.isSelected()){
                    sex=1;
                }
                if (s_woman.isSelected()){
                    sex=0;
                }
                Result result = service.querryContacts(
                        (Group) gnameSearch.getSelectedItem(),
                        s_username.getText(),
                        s_phone.getText(),
                        sex,
                        s_email.getText(),
                        s_address.getText(),
                        s_workunit.getText()
                );
                if (result.getStatus()){
                    data = (ArrayList<Contact>) result.getData();
                    String[] title = {"组别","姓名","性别","电话","邮箱","工作单位","住址"};
                    DefaultTableModel model = new DefaultTableModel(title,0){
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }
                    };
                    for (Contact contact : data){
                        GroupService groupService = new GroupService();
                        Result r = groupService.getGroupByID(contact.getGroupId());
                        Group group = (Group) r.getData();
                        model.addRow(new Object[]{
                                group.getGroupName(),
                                contact.getName(),
                                contact.getSex() == 1 ? "男" : "女",
                                contact.getPhone(),
                                contact.getEmail(),
                                contact.getWorkunit(),
                                contact.getAddress()
                        });
                    }
                    searchTable.setModel(model);
                }else{
                    JOptionPane.showMessageDialog(null,result.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        searchTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount()==2){
                    editContact(searchTable,data);
                }
            }
        });

        searchTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()){
                    JPopupMenu popupMenu = tablePopupMenu(searchTable,data);
                    popupMenu.show(searchTable,e.getX(),e.getY());
                }
            }
        });

    }


    public void init(){
        ImageIcon avatarIcon = new ImageIcon("src/main/resources/img/home/avatar.png");
        avatar.setIcon(avatarIcon);
        ButtonGroup group = new ButtonGroup();
        group.add(s_man);
        group.add(s_woman);
        group.add(allSex);
        allSex.setSelected(true);
        getUserSecssion();
        setToolBar();
        addContactBtn.setEnabled(false);
        GroupListRender rendering = new GroupListRender(this,groupList,groupListPane,gnameSearch,addContactBtn,contactTable);
        rendering.getGroupList();
    }
    public void addGroup(){

        GroupListRender glist = new GroupListRender(this,groupList, groupListPane, gnameSearch, addContactBtn, contactTable);
        GroupDialog groupDialog = new GroupDialog(glist);
        groupDialog.show(null);
    }
    public void addContact(){
        ContactDialog contactDialog = new ContactDialog();
        contactDialog.show(chosengroup,null,contactTable,null);
    }
    public void editContact(JTable table,ArrayList<Contact> tabledata){
        ContactDialog contactDialog = new ContactDialog();

        Contact contact = tabledata.get(table.getSelectedRow());
        GroupService service = new GroupService();
        Result result = service.getGroupByID(contact.getGroupId());
        if (result.getStatus()){
            contactDialog.show((Group) result.getData(),contact,table,searchBtn);
        }else {
            JOptionPane.showMessageDialog(null,result.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
        }
    }
    public void setToolBar(){
        //文件
        JPopupMenu fileMenu = new JPopupMenu();
        JMenuItem importcontact = new JMenuItem("导入联系人");
        Import importDialog = new Import(this,groupList,groupListPane,gnameSearch,addContactBtn,contactTable);
        importcontact.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                importDialog.show();
            }
        });
        JMenuItem exportcontact = new JMenuItem("导出联系人");
        exportcontact.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Export exportDialog = new Export();
                Group chosengroup = (Group) groupList.getSelectedValue();
                exportDialog.show(chosengroup);
            }
        });
        fileMenu.add(importcontact);
        fileMenu.add(exportcontact);
        fileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileMenu.show(fileBtn,0,fileBtn.getHeight());
            }
        });
        //主题
        themeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<ThemeToken> themes = ThemeList.themes;
                JPopupMenu menu = new JPopupMenu();
                for (ThemeToken theme : themes){
                    JMenuItem item = new JMenuItem(theme.getThemeName());
                    item.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            ThemeManager manager = new ThemeManager();
                            Result result = manager.save(theme.createMemo());
                            if (result.getStatus()){
                                IntelliJTheme.setup(Home.class.getResourceAsStream(
                                        theme.getThemePath()
                                ));
                                close();
                                show();
                                JOptionPane.showMessageDialog(null,"主题切换成功","提示",JOptionPane.INFORMATION_MESSAGE);
                            }else {
                                JOptionPane.showMessageDialog(null,result.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });
                    menu.add(item);
                }
                menu.show(themeBtn,0,themeBtn.getHeight());
            }
        });
        //关于
        aboutBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                About about = new About();
                about.show();
            }
        });
    }
    private JPopupMenu tablePopupMenu(JTable table,ArrayList<Contact> tabledata){
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem edit = new JMenuItem("修改");
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editContact(table,tabledata);
            }
        });
        JMenuItem delete = new JMenuItem("删除");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ContactService service = new ContactService();
                Result result = service.deleteContact(contacts.get(table.getSelectedRow()));
                if (result.getStatus()){
                    JOptionPane.showMessageDialog(null,"删除成功");
                    ContactTableRender.refresh(contactTable);
                    searchBtn.doClick();
                }else {
                    JOptionPane.showMessageDialog(null,result.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        JMenu move = new JMenu("移动至");
        GroupListRender grouplist = new GroupListRender(this,groupList,groupListPane,gnameSearch,addContactBtn,contactTable);
        ArrayList<Group> groups = grouplist.getGroupList();
        for (Group group : groups) {
            JMenuItem item = new JMenuItem(group.getGroupName());
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ContactService service = new ContactService();
                    Result result = service.moveContact(tabledata.get(table.getSelectedRow()),group);
                    if (result.getStatus()){
                        JOptionPane.showMessageDialog(null,"移动成功");
                        ContactTableRender.refresh(contactTable);
                        searchBtn.doClick();
                    }else {
                        JOptionPane.showMessageDialog(null,result.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            move.add(item);
        }

        popupMenu.add(edit);
        popupMenu.add(move);
        popupMenu.add(delete);
        return popupMenu;
    }

    public void getUserSecssion() {
        user = UserSecssion.getSecssion().getUser();
        username.setText(user.getUsername());
        email.setText(user.getEmail());
    }

    public void show() {
        ImageIcon icon = new ImageIcon("src/main/resources/img/logo.png");
        frame = new JFrame("Home");
        frame.setIconImage(icon.getImage());
        frame.setContentPane(new Home().homeFrame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        Window.tocenter(frame);
        frame.setVisible(true);

    }
    public void close(){
        frame.dispose();
    }


}
