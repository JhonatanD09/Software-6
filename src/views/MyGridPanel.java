package views;

import javax.swing.*;
import java.awt.*;

public class MyGridPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int COLUMNS_NUMBER = 12;
    private GridBagConstraints constraints;

    public MyGridPanel() {
        setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        generateBasicGrid(constraints);
    }

    protected void addComponent(JComponent component, int posX, int posY, int width, double height) {
        constraints.gridx = posX;
        constraints.gridy = posY;
        constraints.gridwidth = width;
        constraints.weighty = height;
        add(component, constraints);
    }

    protected void addComponentWithInsets(JComponent component, int posX, int posY, int width, double height, Insets insets) {
        constraints.insets = insets;
        constraints.gridx = posX;
        constraints.gridy = posY;
        constraints.weighty = height;
        add(component, constraints);
    }

    //this method generates the 12 column grid
    private void generateBasicGrid(GridBagConstraints constrains) {
        constrains.weightx = 1;
        constrains.gridheight = 1;
        constrains.weighty = 0.01;
        for (int i = 0; i < COLUMNS_NUMBER; i++) {
            constrains.gridx = i;
            add(new JLabel(""), constrains);
        }
    }
}