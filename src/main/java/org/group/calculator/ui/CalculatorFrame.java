package org.group.calculator.ui;

import org.group.calculator.core.converter.EquationConverter;
import org.group.calculator.core.evaluator.Evaluator;
import org.group.calculator.core.model.IterationResult;
import org.group.calculator.core.solver.FixedPointSolver;

import javax.swing.*;
import java.awt.*;

public class CalculatorFrame extends JFrame {

    private final InputPanel inputPanel = new InputPanel();
    private final JTextArea outputArea = new JTextArea(20, 80);

    public CalculatorFrame() {
        super("Fixed Point Iteration Calculator");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        outputArea.setEditable(false);
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        setLayout(new BorderLayout(10, 10));
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        inputPanel.computeBtn.addActionListener(e -> compute());
        inputPanel.clearBtn.addActionListener(e -> outputArea.setText(""));
    }

    private void compute() {
        outputArea.setText("");

        String fExpr = inputPanel.fField.getText().trim();
        String x0Text = inputPanel.x0Field.getText().trim();
        String tolText = inputPanel.tolField.getText().trim();

        double x0;
        double tol;

        try {
            x0 = Double.parseDouble(x0Text);
            tol = Double.parseDouble(tolText);
        } catch (NumberFormatException e) {
            outputArea.append("Invalid numeric input.\n");
            return;
        }

        EquationConverter converter = new EquationConverter();
        Evaluator g = converter.convert(fExpr, 0.1);

        if (!g.isValid()) {
            outputArea.append("Failed to parse generated g(x): " + g.getExpression() + "\n");
            return;
        }

        FixedPointSolver solver = new FixedPointSolver(g, tol, 100);
        IterationResult result = solver.solve(x0);

        outputArea.append("g(x) = " + result.getgExpression() + "\n\n");

        if (result.getErrorMessage() != null) {
            outputArea.append("Error: " + result.getErrorMessage() + "\n\n");
        }

        for (IterationResult.Row row : result.getRows()) {
            outputArea.append(String.format(
                    "%-4d %-15.8f %-12.6f %-12.6f%n",
                    row.index, row.xi, row.eaPercent, row.erPercent
            ));
        }

        if (result.isConverged()) {
            outputArea.append("\nConverged.\n");
        } else {
            outputArea.append("\nNot converged.\n");
        }
    }
}
