package views;

import models.Partition;
import presenters.Events;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import models.Manager;
import models.MyProcess;

public class ReportsPanel extends JPanel {

    private static final String[] COLUMNS = {"Nombre", "Tiempo", "Tama�o", "Bloqueo"};
    private static final String[] TERMINED_COLUMNS = {"Nombre", "Tiempo"};
    private static final String[] INITIAL_PARTITIONS_COLUMNS = {"Nombre", "Tama�o"};
    private static final String[] JOIN_COLUMN = {"Descripcion"};
    private static final String NEW_SIMULATION_BTN_TXT = "Nueva simulacion";
    private static final Color BLUE_COLOR = Color.decode("#2980B9");

    public ReportsPanel(ActionListener listener, ArrayList<Partition> partitions, ArrayList<MyProcess> processes,
                        ArrayList<Partition> initialPartitions, ArrayList<MyProcess> processesTermined,
                        ArrayList<String> joinsInfo, Partition finalPartition ){
        setLayout(new BorderLayout());
        setBackground(Color.decode("#FDFEFE"));
        initTitle();
        addReports(partitions, processes, initialPartitions, processesTermined,joinsInfo,finalPartition);
        initNewSimulationBtn(listener);
    }

    public void addReports(ArrayList<Partition> partitions, ArrayList<MyProcess> processes,
                           ArrayList<Partition> initialPartitions, ArrayList<MyProcess> processesTermined,
                           ArrayList<String> joinsInfo, Partition finalPartition){
        JTabbedPane reports = new JTabbedPane();
        reports.setFont(new Font("Arial", Font.BOLD, 18));
        for(Partition partition : partitions){
            PartitionReportsPanel partitionReportsPanel = new PartitionReportsPanel(partition.getReadyProccess(),
                    partition.getProcessDespachados(), partition.getExecuting(), partition.getProcessExpired(),
                    partition.getProcessLocked(), partition.getProcessTerminated());
            reports.add(partitionReportsPanel, partition.getName());
        }
        TablePanel reportProcessesPanel = new TablePanel(Partition.processInfo(processes), COLUMNS);
        reports.add("Listos", reportProcessesPanel);

        TablePanel reportInitialPartitions = new TablePanel(Manager.processInitialPartitionsInfo(initialPartitions),
                INITIAL_PARTITIONS_COLUMNS);
        reports.add("Particiones iniciales", reportInitialPartitions);

        TablePanel terminedProcessesTable = new TablePanel(Manager.processProcessTermiedInfo(processesTermined), TERMINED_COLUMNS);
        reports.add("Orden terminacion procesos", terminedProcessesTable);
        
        reports.add("Particion final",new JLabel("La particion final es: "+ finalPartition.getName() + " con un tama�o de : "+ finalPartition.getSize()));

        JList<Object> joinsInfoReport = new JList<>(joinsInfo.toArray());
        joinsInfoReport.setFont(new Font("Arial", Font.BOLD, 16));
        reports.add("Condensaciones", joinsInfoReport);

        add(reports, BorderLayout.CENTER);
    }

    private void initNewSimulationBtn(ActionListener listener){
        JButton newSimulationBtn = new JButton(NEW_SIMULATION_BTN_TXT);
        newSimulationBtn.setFont(new Font("Arial", Font.BOLD, 20));
        newSimulationBtn.setForeground(Color.WHITE);
        newSimulationBtn.setBackground(BLUE_COLOR);
        newSimulationBtn.addActionListener(listener);
        newSimulationBtn.setActionCommand(Events.NEW_SIMULATION.toString());
        add(newSimulationBtn, BorderLayout.SOUTH);
    }

    private void initTitle(){
        JLabel titleLb = new JLabel("REPORTES");
        titleLb.setFont(new Font("Arial", Font.BOLD, 16));
        titleLb.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLb, BorderLayout.NORTH);
    }
}