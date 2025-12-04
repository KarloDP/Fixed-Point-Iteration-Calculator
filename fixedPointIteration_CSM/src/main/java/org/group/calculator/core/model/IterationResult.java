package org.group.calculator.core.model;

import java.util.List;

public class IterationResult {

    public static class Row {
        public final int index;
        public final double xi;
        public final double eaPercent;
        public final double erPercent;

        public Row(int index, double xi, double eaPercent, double erPercent) {
            this.index = index;
            this.xi = xi;
            this.eaPercent = eaPercent;
            this.erPercent = erPercent;
        }
    }

    private final String gExpression;
    private final List<Row> rows;
    private final boolean converged;
    private final String errorMessage;

    public IterationResult(String gExpression, List<Row> rows, boolean converged, String errorMessage) {
        this.gExpression = gExpression;
        this.rows = rows;
        this.converged = converged;
        this.errorMessage = errorMessage;
    }

    public String getgExpression() {
        return gExpression;
    }

    public List<Row> getRows() {
        return rows;
    }

    public boolean isConverged() {
        return converged;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
