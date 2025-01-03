package View;

import Model.Contact;
import Model.Group;
import Model.User;
import Service.ContactService;
import Service.GroupService;
import Utils.Database.Result;
import Utils.View.TableObserver.ContactTableRender;
import Utils.View.TableObserver.TableRender;
import Utils.View.Window;
import Utils.UserSecssion.UserSecssion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.*;
import java.util.ArrayList;

public class ContactDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JRadioButton man;
    private JRadioButton woman;
    private JTextField name;
    private JTextField phone;
    private JTextField email;
    private JTextField workunit;
    private JTextField address;
    private JTextArea notes;
    private JComboBox comboBox;
    private JLabel title;
    private static JFrame frame;
    private static Group theGroup;
    private static Contact theContact;
    private static JTable thetable;
    private static JButton theSearchBtn;
    public ContactDialog() {
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(man);
        buttonGroup.add(woman);
        comboBox.removeAllItems();
        GroupService service = new GroupService();
        Result result = service.getUserGroupsByUserID(UserSecssion.getSecssion().getUser().getId());
        ArrayList<Group> groups = (ArrayList<Group>) result.getData();
        for (Group group : groups) {
            comboBox.addItem(group);
        }
        if (theContact == null) {
            comboBox.setSelectedItem(theGroup);
            title.setText("添加联系人");
        }else {
            for (Group group:groups){
                if (group.getId()==theContact.getGroupId()){
                    comboBox.setSelectedItem(group);
                    break;
                }
            }
            title.setText("联系人详情");
            name.setText(theContact.getName());
            phone.setText(theContact.getPhone());
            email.setText(theContact.getEmail());
            workunit.setText(theContact.getWorkunit());
            address.setText(theContact.getAddress());
            notes.setText(theContact.getNotes());
            if (theContact.getSex()==1){
                man.setSelected(true);
            }else {
                woman.setSelected(true);
            }
        }
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // 点击 X 时调用 onCancel()
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // 遇到 ESCAPE 时调用 onCancel()
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        ContactService service = new ContactService();
        User user = UserSecssion.getSecssion().getUser();
        int sex = man.isSelected() ? 1 : 0;

        Contact contact = new Contact(
                theGroup.getId(),
                user.getId(),
                theGroup.getId(),
                name.getText(),
                sex,
                phone.getText(),
                email.getText(),
                workunit.getText(),
                address.getText(),
                notes.getText()
        );
        if (theContact == null) {
            Result result = service.addContact(contact);
            if (result.getStatus()){
                JOptionPane.showMessageDialog(null,"添加成功");
                close();
            }else {
                JOptionPane.showMessageDialog(null,result.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
            }
        }else {
            //如果没有修改直接关闭
            Group group = (Group) comboBox.getSelectedItem();
            if (
                    theGroup.getId()== group.getId()&&
                            theContact.getName().equals(name.getText()) &&
                            theContact.getSex() == sex &&
                            theContact.getPhone().equals(phone.getText()) &&
                            theContact.getEmail().equals(email.getText()) &&
                            theContact.getWorkunit().equals(workunit.getText()) &&
                            theContact.getAddress().equals(address.getText()) &&
                            theContact.getNotes().equals(notes.getText())){
                close();
                return;
            }
            contact.setGroupId(((Group) comboBox.getSelectedItem()).getId());
            contact.setId(theContact.getId());
            Result result = service.updateContact(contact);
            if (result.getStatus()){
                JOptionPane.showMessageDialog(null,"修改成功");
                close();
            }else {
                JOptionPane.showMessageDialog(null,result.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
            }

        }

    }
    private void onCancel() {
        // 必要时在此处添加您的代码
        close();
    }
    public void close(){

            TableRender render = new TableRender(thetable);
            ContactService service = new ContactService();
            Group group = Home.getChosengroup();
            Result result = service.getContactsByGroupID(group.getId());
            if (result.getStatus()){
                ContactTableRender.refresh(thetable);
            }else {
                JOptionPane.showMessageDialog(null,result.getMessage());
            }
            if (theSearchBtn!=null){
                theSearchBtn.doClick();
            }

        frame.dispose();
    }

    public void show(Group group, Contact contact,JTable table,JButton searchBtn) {
        theSearchBtn=searchBtn;
        thetable=table;
        theContact = contact;
        theGroup = group;
        frame = new JFrame();
        frame.setContentPane(new ContactDialog().contentPane);
        frame.pack();
        Window.tocenter(frame);
        frame.setVisible(true);
    }
}
