package views;


import models.MyProcess;
import models.Partition;
import models.Queue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainFrame extends JFrame {

    private static final String TITLE = "Software 6";
    private ActionListener listener;
    private MainPanel mainPanel;

    public MainFrame(ActionListener listener) {
        this.listener = listener;
        setUndecorated(true);
        getContentPane().setLayout(new GridLayout(1,1));
        setTitle(TITLE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initProcessesPanel();
        setExtendedState(MAXIMIZED_BOTH);
    }

    public void initProcessesPanel(){
        mainPanel = new MainPanel(listener);
        add(mainPanel);
    }

    public void updateProcesses(ArrayList<MyProcess> processQueue){
        mainPanel.updateProcesses(processQueue);
    }

    public void initReportsPanel(ArrayList<Partition> partitions, ArrayList<MyProcess> processes,
                                 ArrayList<Partition> initialPartitions, ArrayList<MyProcess> processesTermined,
                                 ArrayList<String> joinsInfo, Partition finalPartition){
        mainPanel.initReportsPanel(partitions, processes, initialPartitions, processesTermined, joinsInfo, finalPartition);
    }

    public void newSimulation(){
        getContentPane().remove(mainPanel);
        mainPanel = new MainPanel(listener);
        add(mainPanel);
        getContentPane().revalidate();
    }
}
