package views;

import exceptions.*;
import models.MyProcess;
import models.Partition;
import models.Queue;
import presenters.Events;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;

public class MainPanel extends MyGridPanel {

    private static final String SIZE_LB_TXT = "Tamaño de memoria: ";
    private JPanel leftPanel;
    private ProcessesPanel processesPanel;
    private ReportsPanel reportsPanel;
    private MyGridPanel startSimulationPanel;
    private ActionListener listener;
    private JTextField sizeTxt;

    public MainPanel(ActionListener listener) {
        this.listener = listener;
        setBackground(Color.decode("#FDFEFE"));
        initExitBtn();
        initLeftPanel();
        initStartSimulationPanel();
    }

    private void initLeftPanel(){
        leftPanel = new JPanel(new BorderLayout());
        initMemorySizePanel();
        processesPanel = new ProcessesPanel(listener);
        leftPanel.add(processesPanel, BorderLayout.CENTER);
        addComponent(leftPanel, 0, 1, 2, 1);
    }

    private void initMemorySizePanel(){
        JPanel sizePanel = new JPanel(new GridLayout(1,2));
        sizePanel.setBackground(Color.decode("#FDFEFE"));
        JLabel sizeLb = new JLabel(SIZE_LB_TXT);
        sizeLb.setFont(new Font("Arial", Font.BOLD, 20));
        sizeTxt = new JTextField();
        sizeTxt.setFont(new Font("Arial", Font.BOLD, 20));
        sizePanel.add(sizeLb);
        sizePanel.add(sizeTxt);
        leftPanel.add(sizePanel, BorderLayout.NORTH);
    }

    private void initExitBtn(){
        MyGridPanel exitBtnPanel = new MyGridPanel();
        exitBtnPanel.setBackground(Color.decode("#34495E"));
        JButton exitBtn = createBtn("Salir", Color.RED, listener, Events.EXIT.toString());
        exitBtnPanel.addComponentWithInsets(exitBtn, 11, 1, 1, 0.1, new Insets(5,0,5,0));
        addComponent(exitBtnPanel, 0,0, 12, 0.005);
    }

    public void initStartSimulationPanel(){
        hideReportsPanel();
        startSimulationPanel = new MyGridPanel();
        JButton startSimulationBtn = createBtn("Iniciar Simulacion", Color.decode("#2980B9"),
                listener, Events.INIT_SIMULATION.toString());
        startSimulationPanel.setBackground(Color.decode("#FDFEFE"));
        startSimulationPanel.addComponent(new JLabel(" "), 0, 3, 12, 0.3);
        startSimulationPanel.addComponent(startSimulationBtn, 5, 4, 5, 0.05);
        startSimulationPanel.addComponent(new JLabel(" "), 0, 5, 12, 0.4);
        addComponent(startSimulationPanel, 2, 1, 9, 1);
        updateUI();
    }

    private void hideReportsPanel(){
        if(reportsPanel != null){
            this.remove(reportsPanel);
        }
    }

    public void initReportsPanel(ArrayList<MyProcess> processes,
                                 ArrayList<Partition> initialPartitions,ArrayList<Partition> terminatedPartitions, ArrayList<MyProcess> processesTermined,
                                 ArrayList<String> joinsInfo, Partition finalPartition){
        this.remove(startSimulationPanel);
        this.remove(leftPanel);
        leftPanel.setBorder(BorderFactory.createMatteBorder(2, 2,2,0, Color.BLACK));
        addComponent(leftPanel, 0, 1, 3, 1);
        reportsPanel = new ReportsPanel(listener, processes, initialPartitions,terminatedPartitions, processesTermined, joinsInfo,finalPartition);
        addComponent(reportsPanel, 3,1,9,0.8);
        updateUI();
    }

    private JButton createBtn(String txt, Color color, ActionListener listener, String command){
        JButton btn = new JButton(txt);
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.addActionListener(listener);
        btn.setActionCommand(command);
        return btn;
    }

    public void updateProcesses(ArrayList<MyProcess> processQueue){
        processesPanel.updateProcesses(processQueue);
    }

    public int getMemorySize() throws EmptyMemorySizeException, InvalidMemorySizeException {
        String memorySize = sizeTxt.getText();
        if(!memorySize.isEmpty()){
            boolean isNumber = memorySize.chars().allMatch(Character::isDigit);
            if(isNumber){
                return Integer.parseInt(memorySize);
            }else{
                throw new InvalidMemorySizeException();
            }
        }else{
            throw new EmptyMemorySizeException();
        }
    }
}