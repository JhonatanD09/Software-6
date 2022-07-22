package presenters;

import exceptions.*;
import models.Manager;
import models.MyProcess;
import views.AddProcessDialog;
import views.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Presenter implements ActionListener {

    private Manager manager;
    private MainFrame mainFrame;
    private AddProcessDialog addProcessDialog;
    private AddProcessDialog editProcessDialog;

    public Presenter() {
        manager = new Manager();
        mainFrame = new MainFrame(this);
        mainFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (Events.valueOf(e.getActionCommand())) {
            case ADD_PROCESS:
                manageAddProcessAction();
                break;
            case EDIT_PROCESS:
                manageEditProcessAction(e);
                break;
            case DELETE_PROCESS:
                manageDeleteProcessAction(e);
                break;
            case ACCEPT_ADD_PROCESS:
                manageAcceptAddProcess();
                break;
            case ACCEPT_EDIT_PROCESS:
                manageAcceptEditProcessAction(e);
                break;
            case CANCEL_ADD_PROCESS:
                manageCancelAddProcessAction();
                break;
            case CANCEL_EDIT_PROCESS:
                manageCancelEditProcessAction();
                break;
            case INIT_SIMULATION:
                manageInitSimulationAction();
                break;
            case NEW_SIMULATION:
                manageNewSimulationAction();
                break;
            case EXIT:
                System.exit(0);
                break;
        }
    }

    private void manageNewSimulationAction() {
        manager = new Manager();
        mainFrame.newSimulation();
    }

    private void manageInitSimulationAction() {
        if(!manager.getProcesses().isEmpty()){
            manager.initSimulation();
            mainFrame.initReportsPanel(manager.getPartitions(), manager.getProcesses(), manager.getPartitions(),
                    manager.getProcessesTermined(), manager.getJoinsReports(),manager.getFinalPartition());
        }else{
            JOptionPane.showMessageDialog(mainFrame, "Debe haber almenos un proceso para poder iniciar la simulacion",
                    "ALERTA", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void manageEditProcessAction(ActionEvent e) {
        String processName = ((JButton) e.getSource()).getName();
        MyProcess process = manager.search(processName);
        editProcessDialog = new AddProcessDialog(this, true);
        editProcessDialog.setInitialInfo(process.getName(), String.valueOf(process.getTime()),
                String.valueOf(process.getSize()), process.isLocked());
        editProcessDialog.setVisible(true);
    }

    private void manageCancelEditProcessAction() {
        editProcessDialog.dispose();
    }

    private void manageAcceptEditProcessAction(ActionEvent e) {
        try{
            String actualName = ((JButton) e.getSource()).getName();
            if(!actualName.equals(editProcessDialog.getProcessName())){
                manager.verifyProcessName(editProcessDialog.getProcessName());
            }
            manager.editProcess(actualName, editProcessDialog.getProcessName(), editProcessDialog.getProcessTime(),
                    editProcessDialog.getProcessSize(), editProcessDialog.getIsBlocked());
            mainFrame.updateProcesses(manager.getProcesses());
            editProcessDialog.dispose();
        }catch(EmptyProcessNameException | RepeatedNameException | EmptyProcessSizeException |
               EmptyProcessTimeException | InvalidSizeException | InvalidTimeException ex){
            JOptionPane.showMessageDialog(mainFrame, ex.getMessage());
        }
    }

    private void manageDeleteProcessAction(ActionEvent e) {
        String processName = ((JButton) e.getSource()).getName();
        if(manager.deleteProccess(processName)){
            mainFrame.updateProcesses(manager.getProcesses());
            JOptionPane.showMessageDialog(mainFrame, "Proceso eliminado con exito", "Eliminar",
                    JOptionPane.INFORMATION_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(mainFrame, "No se ha podido eliminar el proceso",
                    "ERROR!!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void manageAcceptAddProcess() {
        try {
            String processName = addProcessDialog.getProcessName();
            manager.verifyProcessName(processName);
            long time = addProcessDialog.getProcessTime();
            long size = addProcessDialog.getProcessSize();
            boolean isBlocked = addProcessDialog.getIsBlocked();
            manager.addProcess(new MyProcess(processName, time, size, isBlocked));
            mainFrame.updateProcesses(manager.getProcesses());
            addProcessDialog.dispose();
        } catch (EmptyProcessNameException | EmptyProcessTimeException | EmptyProcessSizeException |
                 RepeatedNameException | InvalidTimeException | InvalidSizeException ex) {
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "ERROR!!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void manageAddProcessAction() {
        addProcessDialog = new AddProcessDialog(this, false);
        addProcessDialog.setVisible(true);
    }

    private void manageCancelAddProcessAction() {
        addProcessDialog.dispose();
    }
}
