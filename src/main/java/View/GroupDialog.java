package View;

import Model.Group;
import Model.User;
import Service.GroupService;
import Utils.Database.Result;
import Utils.View.GroupObserver.OnGroupChangedListener;
import Utils.View.Window;
import Utils.UserSecssion.UserSecssion;

import javax.swing.*;
import java.awt.event.*;

public class GroupDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField groupname;
    private JLabel title;
    private static JFrame frame;
    private static Group g;
    private JPanel addGroupDailog;
    private OnGroupChangedListener Listener;
    public GroupDialog(OnGroupChangedListener onGroupChangedListener) {
        this.Listener = onGroupChangedListener;
        if (g == null){
            title.setText("添加分组");
            setTitle("添加分组");
        }else {
            groupname.setText(g.getGroupName());
            title.setText("编辑分组");
            setTitle("编辑分组");
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
        User user = UserSecssion.getSecssion().getUser();
        GroupService service = new GroupService();
        if (g == null) {
            Result result = service.addUserGroup(user.getId(), groupname.getText());
            if (result != null) {
                if (result.getStatus()) {
                    JOptionPane.showMessageDialog(frame, "分组添加成功");
                    close();
                } else {
                    JOptionPane.showMessageDialog(frame, result.getMessage());
                }
            }
        } else {

            if (groupname.getText().equals(g.getGroupName())){
                close();
                return;
            }
            Group newGroup = new Group(g.getId(), user.getId(), groupname.getText(), g.getIsDefault());
            Result result = service.updateUserGroup(newGroup);
            if (result != null) {
                if (result.getStatus()) {
                    JOptionPane.showMessageDialog(frame, "修改成功");
                    close();
                } else {
                    JOptionPane.showMessageDialog(frame, result.getMessage());
                }
            }
        }
    }


    private void onCancel() {
        close();
    }

    public void close(){
        Listener.onGroupChanged();
        frame.dispose();
    }
    public void show(Group group) {
        g = group;
        frame = new JFrame();
        frame.setContentPane(new GroupDialog(Listener).contentPane);
        frame.pack();
        Window.tocenter(frame);
        frame.setVisible(true);
    }

}
