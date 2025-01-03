package View;

import Utils.Database.Result;
import Utils.ExcelTool.ExcelContact;
import Utils.ExcelTool.ExcelFacade;
import Utils.View.GroupObserver.GroupListRender;
import Utils.View.Window;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class Import extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField filepath;
    private JButton open;
    private JButton download;
    private JTable table;
    private static JFrame frame;
    private static ArrayList<ExcelContact> tabledata;
    private static JList groupList;
    private static JScrollPane groupListPane;
    private static JComboBox gnameSearch;
    private static JButton addContactBtn;
    private static JTable contactTable;
    private static Home home;
    public Import() {
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
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //打开文件选择窗口，指定文件类型为.xls或.xlsx
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                    public boolean accept(File f) {
                        return f.isDirectory()|| f.getName().endsWith(".xls") || f.getName().endsWith(".xlsx");
                    }
                    public String getDescription() {
                        return "Excel文件(*.xls,*.xlsx)";
                    }
                });
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    filepath.setText(fileChooser.getSelectedFile().getAbsolutePath());
                    ExcelFacade excelFacade = new ExcelFacade();
                    Result result = excelFacade.readExcel(filepath.getText());
                    tabledata = (ArrayList<ExcelContact>) result.getData();
                    String[] title={"分组","姓名","性别","电话","邮箱","工作单位","家庭住址","备注"};
                    DefaultTableModel model = new DefaultTableModel(title,0){
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }
                    };
                    //去除第一行
                    tabledata.remove(0);
                    for (ExcelContact excelContact : tabledata){
                        Object[] row = {
                                excelContact.getGroupName(),
                                excelContact.getName(),
                                excelContact.getSex()==1?"男":"女",
                                excelContact.getPhone(),
                                excelContact.getEmail(),
                                excelContact.getWorkunit(),
                                excelContact.getAddress(),
                                excelContact.getNotes()
                        };
                        model.addRow(row);
                    }
                    table.setModel(model);
                }
            }
        });
        download.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //打开文件选择框,复制resources/Excel/template.xls到指定位置
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    String path = fileChooser.getSelectedFile().getAbsolutePath();
                    String templatePath = "src/main/resources/Excel/template.xlsx";
                    String targetPath = path + "/LinkUpTemplate.xlsx";
                    //使用输入输出流，将template.xls复制到指定位置
                    try {
                        File outputfile = new File(targetPath);
                        FileInputStream inputStream = new FileInputStream(templatePath);
                        FileOutputStream outputStream = new FileOutputStream(outputfile);
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }
                        inputStream.close();
                        outputStream.close();
                        JOptionPane.showMessageDialog(null,"模板文件已生成到"+targetPath,"成功",JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException exception){
                        JOptionPane.showMessageDialog(null,exception.getMessage());
                    }
                }
            }
        });
    }

    public Import(Home home, JList groupList, JScrollPane groupListPane, JComboBox gnameSearch, JButton addContactBtn, JTable contactTable) {
        this.home=home;
        this.groupList = groupList;
        this.groupListPane = groupListPane;
        this.gnameSearch = gnameSearch;
        this.addContactBtn = addContactBtn;
        this.contactTable = contactTable;
    }

    private void onOK() {
        ExcelFacade excelFacade = new ExcelFacade();
        if (tabledata==null){
            close();
            return;
        }
        Result result = excelFacade.saveContactsToDatabase(tabledata);
        if (result.getStatus()){
            GroupListRender groupListRender = new GroupListRender(home,groupList, groupListPane, gnameSearch, addContactBtn, contactTable);
            groupListRender.onGroupChanged();
            JOptionPane.showMessageDialog(null,"导入成功");
            close();
        }else {
            JOptionPane.showMessageDialog(null,result.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        // 必要时在此处添加您的代码
        close();
    }
    public void close(){
        frame.dispose();
    }

    public  void show(){
        frame = new JFrame("导入通讯录");
        frame.setContentPane(new Import().contentPane);
        frame.pack();
        Window.tocenter(frame);
        frame.setVisible(true);
    }
}
