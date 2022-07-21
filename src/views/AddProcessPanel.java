package views;

import exceptions.*;
import presenters.Events;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AddProcessPanel extends MyGridPanel{

    private JTextField processNameTxt;
    private JTextField processTimeTxt;
    private JTextField processSizeTxt;
    private JCheckBox isBlockedCb;
    private JButton addBtn;

    public AddProcessPanel(ActionListener listener, boolean isEditing) {
        setBackground(Color.decode("#FDFEFE"));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        initComponents(listener, isEditing);
    }

    private void initComponents(ActionListener listener, boolean isEditing) {
        initTitle(isEditing);
        initProcessNameTxt();
        initProcessTimeTxt();
        initProcessSizeTxt();
        initIsBlockedCb();
        if(isEditing){
            initButtons(listener, Events.ACCEPT_EDIT_PROCESS.toString(), Events.CANCEL_EDIT_PROCESS.toString(), isEditing);
        }else{
            initButtons(listener, Events.ACCEPT_ADD_PROCESS.toString(), Events.CANCEL_ADD_PROCESS.toString(), isEditing);
        }
    }

    private void initTitle(boolean isEditing){
        String title = isEditing ? "Editar Proceso" : "Nuevo Proceso";
        JTextArea titleTa = new JTextArea();
        titleTa.setText(title);
        titleTa.setEditable(false);
        titleTa.setFont(new Font("Arial", Font.BOLD, 16));
        addComponent(new JLabel(" "), 0, 0, 11, 0.1);
        addComponent(titleTa, 4, 1, 4, 0.2);
        addComponent(new JLabel(" "), 0, 3, 11, 0.1);
    }

    private void initProcessNameTxt(){
        JLabel nameLb = createLb("   Nombre:", new Font("Arial", Font.BOLD, 14));
        addComponent(nameLb, 2, 5, 2, 0.1);
        processNameTxt = new JTextField();
        addComponent(processNameTxt, 5, 5, 5, 0.1);
        addComponent(new JLabel(" "), 0, 6, 11, 0.1);
    }

    private void initProcessTimeTxt() {
        JLabel timeLb = createLb("   Tiempo: ", new Font("Arial", Font.BOLD, 14));
        addComponent(timeLb, 2, 7, 2, 0.1);
        processTimeTxt = new JTextField();
        processTimeTxt.setText("");
        addComponent(processTimeTxt, 5, 7, 5, 0.1);
        addComponent(new JLabel(" "), 0, 8, 11, 0.1);
    }

    private void initProcessSizeTxt(){
        JLabel sizeLb = createLb("   Tamaño: ", new Font("Arial", Font.BOLD, 14));
        addComponent(sizeLb, 2, 9, 2, 0.1);
        processSizeTxt = new JTextField();
        processSizeTxt.setText("");
        addComponent(processSizeTxt, 5, 9, 5, 0.1);
        addComponent(new JLabel(" "), 0, 10, 11, 0.1);
    }

    private void initIsBlockedCb(){
        JLabel isBlockedLb = createLb("   Bloqueo: ", new Font("Arial", Font.BOLD, 14));
        addComponent(isBlockedLb, 2, 11, 2, 0.1);
        isBlockedCb = new JCheckBox();
        isBlockedCb.setBackground(Color.WHITE);
        isBlockedCb.setHorizontalAlignment(SwingConstants.CENTER);
        addComponent(isBlockedCb, 7, 11, 1, 0.1);
        addComponent(new JLabel(" "), 0, 12, 11, 0.1);
    }

    private void initButtons(ActionListener listener, String acceptEvent, String cancelEvent, boolean isEditing){
        String addBtnTxt = isEditing ? "Editar" : "Agregar";
        addBtn = createBtn(addBtnTxt, Color.decode("#00D48B"), listener, acceptEvent);
        addComponent(addBtn, 3, 13, 2, 0.12);
        JButton cancelBtn = createBtn("Cancelar", Color.decode("#FA512D"), listener, cancelEvent);
        addComponent(cancelBtn, 7, 13, 2, 0.12);
        addComponent(new JLabel(" "), 0, 14, 11, 0.05);
    }

    private JLabel createLb(String txt, Font font){
        JLabel lb = new JLabel(txt);
        lb.setFont(font);
        return lb;
    }

    private JButton createBtn(String txt, Color color, ActionListener listener, String command){
        JButton btn = new JButton(txt);
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFont(new Font("Arial", Font.BOLD, 15));
        btn.addActionListener(listener);
        btn.setActionCommand(command);
        return btn;
    }

    public String getProcessName() throws EmptyProcessNameException {
        if(!processNameTxt.getText().isEmpty()){
            return processNameTxt.getText();
        }else{
            throw new EmptyProcessNameException();
        }
    }

    public long getProcessTime() throws EmptyProcessTimeException, InvalidTimeException {
        String time = processTimeTxt.getText();
        if(!time.isEmpty()){
            boolean isNumber = time.chars().allMatch(Character::isDigit);
            if(isNumber){
                return Long.parseLong(time);
            }else{
                throw new InvalidTimeException();
            }
        }else{
            throw new EmptyProcessTimeException();
        }
    }

    public long getProcessSize() throws EmptyProcessSizeException, InvalidSizeException {
        String size = processSizeTxt.getText();
        if(!size.isEmpty()){
            boolean isNumber = size.chars().allMatch(Character::isDigit);
            if(isNumber){
                return Long.parseLong(size);
            }else{
                throw new InvalidSizeException();
            }
        }else{
            throw new EmptyProcessSizeException();
        }
    }

    public void setInitialInfo(String name, String time, String size, boolean isLocked){
        processNameTxt.setText(name);
        processTimeTxt.setText(time);
        processSizeTxt.setText(size);
        isBlockedCb.setSelected(isLocked);
        addBtn.setName(name);
    }

    public boolean getIsBlocked(){
        return isBlockedCb.isSelected();
    }
}