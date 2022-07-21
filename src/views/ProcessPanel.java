package views;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import models.MyProcess;
import presenters.Events;


public class ProcessPanel extends MyGridPanel{

    private static final String LB_NAME_TXT = "Nombre: ";
    private static final String LB_TIME_TXT = "Tiempo: ";
    private static final String LB_SIZE_TXT = "Tamaño: ";
    private static final String LB_IS_BLOCKED_TXT = "Bloqueo: ";
    private static final String EDIT_BTN_TXT = "Editar";
    private static final String DELETE_BTN_TXT = "Eliminar";

    public ProcessPanel(MyProcess process ,ActionListener listener){
        setBackground(Color.decode("#FDFEFE"));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        initComponents(process, listener);
    }

    private void initComponents(MyProcess process, ActionListener listener){
        addComponent(new JLabel(" "), 0, 0, 12, 0.2);
        JLabel lbName = new JLabel(LB_NAME_TXT + process.getName());
        lbName.setFont(new Font("Arial", Font.BOLD, 15));
        addComponent(lbName, 1, 1, 1, 1);

        JLabel lbTime = new JLabel(LB_TIME_TXT + process.getTime());
        lbTime.setFont(new Font("Arial", Font.BOLD, 15));
        addComponent(lbTime, 3,1,1,1);

        JLabel lbSize = new JLabel(LB_SIZE_TXT + process.getSize());
        lbSize.setFont(new Font("Arial", Font.BOLD, 15));
        addComponent(lbSize, 5,1,1,1);

        initIsLocked(process.isLocked());
        initButtons(listener, process.getName());

        addComponent(new JLabel(" "), 0, 2, 12, 0.2);
    }

    private void initIsLocked(boolean isLocked){
        JPanel isLockedPanel = new JPanel(new GridLayout(1,2));
        isLockedPanel.setBackground(Color.decode("#FDFEFE"));
        JLabel isLockedLb = new JLabel(LB_IS_BLOCKED_TXT);
        isLockedLb.setFont(new Font("Arial", Font.BOLD, 15));
        isLockedPanel.add(isLockedLb);
        isLockedPanel.add(new SymbolPanel(isLocked, false));
        addComponent(isLockedPanel, 7, 1,1,1);
    }

    private void initButtons(ActionListener listener, String processName){
        String info = processName;
        JButton editBtn = createBtn(EDIT_BTN_TXT, Color.BLUE, listener, Events.EDIT_PROCESS.toString(), info);
        addComponentWithInsets(editBtn, 9,1,1,0.5, new Insets(0,0,0, 10));
        JButton deleteButton = createBtn(DELETE_BTN_TXT, Color.RED, listener, Events.DELETE_PROCESS.toString(), info);
        addComponent(deleteButton, 10, 1,1,0.5);
    }

    private JButton createBtn(String txt, Color color, ActionListener listener, String command, String info){
        JButton btn = new JButton(txt);
        btn.setName(info);
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.addActionListener(listener);
        btn.setActionCommand(command);
        return btn;
    }
}
