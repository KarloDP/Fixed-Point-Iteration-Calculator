package org.group.calculator.core.converter;

import org.group.calculator.core.evaluator.Evaluator;
import org.group.calculator.core.evaluator.Exp4jEvaluator;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EquationConverter {

    public Evaluator convert(String fExpr, double lambda) {
        Optional<String> poly = tryPolynomial(fExpr);
        if (poly.isPresent()) return new Exp4jEvaluator(poly.get());

        Optional<String> generic = tryGeneric(fExpr);
        if (generic.isPresent()) return new Exp4jEvaluator(generic.get());

        String fallback = "(x - " + lambda + " * (" + fix(fExpr) + "))";
        return new Exp4jEvaluator(fallback);
    }

    private Optional<String> tryGeneric(String f) {
        if (f == null) return Optional.empty();
        String s = f.replaceAll("\\s+", "");

        Matcher m1 = Pattern.compile("^(.*)-x$").matcher(s);
        if (m1.find()) return Optional.of(fix(m1.group(1)));

        Matcher m2 = Pattern.compile("^x-(.*)$").matcher(s);
        if (m2.find()) return Optional.of(fix(m2.group(1)));

        return Optional.empty();
    }

    private Optional<String> tryPolynomial(String f) {
        Optional<Map<Integer, Double>> parsed = PolynomialParser.parse(f);
        if (parsed.isEmpty()) return Optional.empty();

        Map<Integer, Double> coeffs = parsed.get();
        double a1 = coeffs.getOrDefault(1, 0.0);
        if (Math.abs(a1) < 1e-15) return Optional.empty();

        List<Integer> powers = new ArrayList<>(coeffs.keySet());
        powers.sort(Collections.reverseOrder());

        StringBuilder other = new StringBuilder();
        for (int p : powers) {
            if (p == 1) continue;
            double c = coeffs.get(p);
            if (Math.abs(c) < 1e-15) continue;

            if (other.length() > 0) {
                other.append(c >= 0 ? " + " : " - ");
            } else if (c < 0) {
                other.append("-");
            }

            double absC = Math.abs(c);
            if (p == 0) {
                other.append(num(absC));
            } else {
                if (absC != 1.0) other.append(num(absC)).append("*");
                other.append("x");
                if (p > 1) other.append("^").append(p);
            }
        }

        if (other.length() == 0) other.append("0");

        String rhs = "-(" + other + ")/(" + num(a1) + ")";
        return Optional.of(fix(rhs));
    }

    private String fix(String s) {
        if (s == null) return "";

        String out = s.replace("Math.", "");

        Pattern p1 = Pattern.compile("e\\s*\\^\\s*\\(([^)]+)\\)");
        Matcher m1 = p1.matcher(out);
        StringBuffer sb1 = new StringBuffer();
        while (m1.find()) {
            m1.appendReplacement(sb1, "exp(" + m1.group(1) + ")");
        }
        m1.appendTail(sb1);
        out = sb1.toString();

        Pattern p2 = Pattern.compile("e\\s*\\^\\s*([A-Za-z0-9_.()]+)");
        Matcher m2 = p2.matcher(out);
        StringBuffer sb2 = new StringBuffer();
        while (m2.find()) {
            m2.appendReplacement(sb2, "exp(" + m2.group(1) + ")");
        }
        m2.appendTail(sb2);

        return sb2.toString();
    }

    private String num(double d) {
        if (d == (long) d) return Long.toString((long) d);
        return Double.toString(d);
    }
}