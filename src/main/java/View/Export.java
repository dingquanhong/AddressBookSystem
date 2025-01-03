package View;

import Model.Contact;
import Model.Group;
import Model.User;
import Service.ContactService;
import Utils.Database.Result;
import Utils.ExcelTool.ExcelFacade;
import Utils.UserSecssion.UserSecssion;
import Utils.View.Window;


import javax.swing.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Export extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JRadioButton currentGroup;
    private JRadioButton allGroup;
    private static JFrame frame;
    private static Group group;
    public Export() {
        ButtonGroup btngroup = new ButtonGroup();
        btngroup.add(currentGroup);
        btngroup.add(allGroup);
        if (group==null){
            currentGroup.setEnabled(false);
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
        Result result = null;
        if (currentGroup.isSelected()){
            ContactService service = new ContactService();
            result = service.getContactsByGroupID(group.getId());
        }else {
            ContactService service = new ContactService();
            User user = UserSecssion.getSecssion().getUser();
            result = service.getUserContacts(user);
        }
        if (result.getStatus()){
            ExcelFacade excelFacade = new ExcelFacade();
            //打开路径选择框,导出
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                //加上导出时间
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
                String timestamp = now.format(formatter);
                String filename = "";
                if (group!=null){
                    filename = "/Linkup_"+group.getGroupName()+timestamp+".xlsx";
                }else {
                    filename = "/Linkup_所有分组"+timestamp+".xlsx";
                }
                Result exportResult = excelFacade.exportExcel((ArrayList<Contact>) result.getData(), filePath+filename);
                if (exportResult.getStatus()){
                    JOptionPane.showMessageDialog(null,"已成功导出至"+filePath+filename);
                }else {
                    JOptionPane.showMessageDialog(null,exportResult.getMessage());
                }
            }
        }else {
            JOptionPane.showMessageDialog(null,result.getMessage());
        }

    }

    private void onCancel() {
        close();
    }
    public void close() {
        frame.dispose();
    }
    public void show(Group currentGroup) {
        group = currentGroup;
        frame = new JFrame("导出通讯录");
        frame.setContentPane(new Export().contentPane);
        frame.pack();
        Window.tocenter(frame);
        frame.setVisible(true);
    }
}
