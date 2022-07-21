package views;

import models.MyProcess;
import models.Partition;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PartitionReportsPanel extends JPanel {

    private static final String[] COLUMNS = {"Nombre", "Tiempo", "Tamaño", "Bloqueo"};
    private static final String[] OVER_SIZE_COLUMNS = {"Nombre", "Descripcion"};

    public PartitionReportsPanel(ArrayList<MyProcess> readyProcess, ArrayList<MyProcess> dispatchedProcess,
                                 ArrayList<MyProcess> executingProcess, ArrayList<MyProcess> expiredProcess,
                                 ArrayList<MyProcess> lockedProcess, ArrayList<MyProcess> terminatedProcess){
        setLayout(new BorderLayout());
        setBackground(Color.decode("#FDFEFE"));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        JTabbedPane reports = new JTabbedPane();
        reports.setFont(new Font("Arial", Font.PLAIN, 18));
        reports.setBackground(Color.decode("#FDFEFE"));
        TablePanel readyTable = new TablePanel(Partition.processInfo(readyProcess), COLUMNS);
        reports.add("Listos", readyTable);

        TablePanel dispatchedTable = new TablePanel(Partition.processInfo(dispatchedProcess), COLUMNS);
        reports.add("Despachados", dispatchedTable);

        TablePanel executingTable = new TablePanel(Partition.processInfo(executingProcess), COLUMNS);
        reports.add("En ejecucion", executingTable);

        TablePanel expiredTable = new TablePanel(Partition.processInfo(expiredProcess), COLUMNS);
        reports.add("Tiempo expirado", expiredTable);

        TablePanel lockedTable = new TablePanel(Partition.processInfo(lockedProcess), COLUMNS);
        reports.add("Bloqueados", lockedTable);

        TablePanel terminatedTable = new TablePanel(Partition.processInfo(terminatedProcess), COLUMNS);
        reports.add("Terminados", terminatedTable);

        add(reports, BorderLayout.CENTER);
    }
}
