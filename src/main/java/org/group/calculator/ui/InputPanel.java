package org.group.calculator.ui;

import javax.swing.*;
import java.awt.*;

public class InputPanel extends JPanel {

    public final JTextField fField = new JTextField("e^(-x) - x");
    public final JTextField x0Field = new JTextField("0.0");
    public final JTextField tolField = new JTextField("1.0");

    public final JButton computeBtn = new JButton("Compute");
    public final JButton clearBtn = new JButton("Clear Output");

    public InputPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0;
        add(new JLabel("f(x):"), c);
        c.gridx = 1; c.weightx = 1.0;
        add(fField, c);

        c.gridx = 0; c.gridy = 1; c.weightx = 0;
        add(new JLabel("Initial x0:"), c);
        c.gridx = 1;
        add(x0Field, c);

        c.gridx = 0; c.gridy = 2;
        add(new JLabel("Tolerance (%):"), c);
        c.gridx = 1;
        add(tolField, c);

        c.gridx = 0; c.gridy = 3; c.gridwidth = 2;
        add(computeBtn, c);

        c.gridy = 4;
        add(clearBtn, c);
    }
}
