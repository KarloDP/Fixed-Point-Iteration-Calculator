package org.group.calculator.core.solver;

import org.group.calculator.core.evaluator.Evaluator;
import org.group.calculator.core.model.IterationResult;
import org.group.calculator.core.model.IterationResult.Row;

import java.util.ArrayList;
import java.util.List;

public class FixedPointSolver {

    private final Evaluator g;
    private final double tolerancePercent;
    private final int maxIterations;

    public FixedPointSolver(Evaluator g, double tolerancePercent, int maxIterations) {
        this.g = g;
        this.tolerancePercent = tolerancePercent;
        this.maxIterations = maxIterations;
    }

    public IterationResult solve(double x0) {
        if (g == null || !g.isValid()) {
            return new IterationResult(null, List.of(), false, "Invalid g(x) expression.");
        }

        List<Row> rows = new ArrayList<>();
        rows.add(new Row(0, x0, 100.0, 100.0));

        double prev = x0;

        for (int i = 1; i <= maxIterations; i++) {
            double next = g.evaluate(prev);
            if (Double.isNaN(next) || Double.isInfinite(next)) {
                return new IterationResult(
                        g.getExpression(),
                        rows,
                        false,
                        "g(x) produced an invalid value at iteration " + i
                );
            }

            double diff = Math.abs(next - prev);
            double ea = (Math.abs(next) > 0) ? (diff / Math.abs(next)) * 100.0 : diff * 100.0;
            double er = (Math.abs(prev) > 0) ? (diff / Math.abs(prev)) * 100.0 : ea;

            rows.add(new Row(i, next, ea, er));

            if (ea <= tolerancePercent) {
                return new IterationResult(
                        g.getExpression(),
                        rows,
                        true,
                        null
                );
            }

            prev = next;
        }

        return new IterationResult(
                g.getExpression(),
                rows,
                false,
                "Did not converge within " + maxIterations + " iterations."
        );
    }
}
