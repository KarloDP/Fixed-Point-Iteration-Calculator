package org.group.calculator.core.converter;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PolynomialParser {

    private static final Pattern TERM_PATTERN = Pattern.compile("([+-]?[^+-]+)");

    public static Optional<Map<Integer, Double>> parse(String f) {
        if (f == null || f.isBlank()) return Optional.empty();

        String s = f.replaceAll("(?i)f\\s*\\(\\s*x\\s*\\)\\s*=", "")
                .replaceAll("=\\s*0", "")
                .replaceAll("\\s+", "");

        Matcher m = TERM_PATTERN.matcher(s);
        Map<Integer, Double> coeffs = new HashMap<>();
        boolean hasX = false;

        while (m.find()) {
            String term = m.group(1);
            if (term == null || term.isBlank()) continue;

            term = term.replace("*", "");

            int sign = 1;
            if (term.startsWith("+")) term = term.substring(1);
            else if (term.startsWith("-")) {
                sign = -1;
                term = term.substring(1);
            }

            double coeff;
            int power;

            if (term.contains("x")) {
                hasX = true;
                int idx = term.indexOf("x");
                String coeffPart = term.substring(0, idx);

                if (coeffPart.isEmpty()) coeff = 1.0;
                else {
                    try { coeff = Double.parseDouble(coeffPart); }
                    catch (NumberFormatException e) { return Optional.empty(); }
                }

                if (term.length() > idx + 1 && term.charAt(idx + 1) == '^') {
                    String pStr = term.substring(idx + 2);
                    try { power = Integer.parseInt(pStr); }
                    catch (NumberFormatException e) { return Optional.empty(); }
                } else {
                    power = 1;
                }
            } else {
                try { coeff = Double.parseDouble(term); }
                catch (NumberFormatException e) { return Optional.empty(); }
                power = 0;
            }

            coeff *= sign;
            coeffs.put(power, coeffs.getOrDefault(power, 0.0) + coeff);
        }

        if (!hasX) return Optional.empty();
        return Optional.of(coeffs);
    }
}
