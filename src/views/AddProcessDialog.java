package views;


import exceptions.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AddProcessDialog extends JDialog {

    private AddProcessPanel addProcessPanel;

    public AddProcessDialog(ActionListener listener, boolean isEditing) {
        setInfo();
        addProcessPanel = new AddProcessPanel(listener, isEditing);
        add(addProcessPanel);
    }

    private void setInfo(){
        setSize(400, 400);
        setModal(true);
        setLayout(new BorderLayout());
        setResizable(false);
        setUndecorated(true);
        setLocationRelativeTo(null);
    }

    public String getProcessName() throws EmptyProcessNameException {
        return addProcessPanel.getProcessName();
    }

    public long getProcessTime() throws EmptyProcessTimeException, InvalidTimeException {
        return addProcessPanel.getProcessTime();
    }

    public long getProcessSize() throws EmptyProcessSizeException, InvalidSizeException {
        return addProcessPanel.getProcessSize();
    }

    public boolean getIsBlocked(){
        return addProcessPanel.getIsBlocked();
    }

    public void setInitialInfo(String name, String time, String size,boolean isLocked){
        addProcessPanel.setInitialInfo(name, time, size, isLocked);
    }
}
