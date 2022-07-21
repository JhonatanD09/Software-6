package views;

import models.MyProcess;
import presenters.Events;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ProcessesPanel extends MyGridPanel {

    private static final String TITLE = "Lista de Procesos";
    private static final String TXT_ADD_PROCESS_BTN = "Agregar Proceso";
    private JPanel processes;
    private JScrollPane scrollPane;
    private ActionListener listener;

    public ProcessesPanel(ActionListener listener) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        setBackground(Color.decode("#FDFEFE"));
        initComponents(listener);
    }

    private void initComponents(ActionListener listener){
        scrollPane = new JScrollPane();
        this.listener = listener;
        addTitle();
        initProcessesPanel();
        initAddProcessBtn();
    }


    public void addTitle(){
        JLabel titleLb = new JLabel(TITLE);
        titleLb.setOpaque(true);
        titleLb.setBackground(Color.decode("#16A085"));
        titleLb.setForeground(Color.WHITE);
        titleLb.setFont(new Font("Arial", Font.BOLD, 20));
        titleLb.setHorizontalAlignment(SwingConstants.CENTER);
        addComponent(titleLb, 0, 0, 12, 0.03);
    }

    private void initProcessesPanel(){
        processes = new JPanel(new GridLayout(1,1));
        processes.setBackground(Color.decode("#FDFEFE"));
        for (int i = 0; i < 10; i++) {
            processes.add(new JLabel(" "));
        }
        scrollPane.add(processes);
        addComponent(processes, 0, 1, 12, 0.8);
    }

    public void updateProcesses(ArrayList<MyProcess> processQueue){
        removeAll();
        addTitle();
        processes = new JPanel(new GridLayout(processQueue.size(), 1));
        verifyRowsNumber(processQueue);
        scrollPane = new JScrollPane(processes);
        addComponent(scrollPane, 0, 1, 12, 0.8);
        initAddProcessBtn();
        updateUI();
    }

    private void verifyRowsNumber(ArrayList<MyProcess> processQueue){
        if(processQueue.size() < 10){
            processes = new JPanel(new GridLayout(10, 1, 5, 5));
            processes.setBackground(Color.decode("#FDFEFE"));
            addProcesses(processQueue);
            for (int i = 0; i < (8 - processQueue.size()); i++) {
                processes.add(new JLabel(" "));
            }
        }else{
            processes = new JPanel(new GridLayout(processQueue.size(), 1, 5,5));
            processes.setBackground(Color.decode("#FDFEFE"));
            addProcesses(processQueue);
        }
    }

    private void addProcesses(ArrayList<MyProcess> processQueue){
        for(MyProcess process : processQueue){
            ProcessPanel processPanel = new ProcessPanel(process, listener);
            processes.add(processPanel);
        }
    }

    public void initAddProcessBtn(){
        JButton addProcessBtn = new JButton(TXT_ADD_PROCESS_BTN);
        addProcessBtn.setBackground(Color.decode("#2980B9"));
        addProcessBtn.setForeground(Color.WHITE);
        addProcessBtn.setFont(new Font("Arial", Font.BOLD, 16));
        addProcessBtn.setPreferredSize(new Dimension(500, 40));
        addProcessBtn.addActionListener(listener);
        addProcessBtn.setActionCommand(Events.ADD_PROCESS.toString());
        addComponent(addProcessBtn, 0, 3, 12, 0.01);
    }
}
