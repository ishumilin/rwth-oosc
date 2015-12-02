package oosc.assignment02.polymorphism;

import java.util.Stack;

public class PolymorphismDemo {
    public static void main(String[] args) {
        String expression = "( 2 - 5 ) - ( 2 + 3 )";
        double result = Evaluator.evaluate(expression.split(" "));
        System.out.println("Result: " + result);
    }

    public static final class Evaluator {
        public static double evaluate(String[] tokens) {
            Stack<String> ops = new Stack<String>();
            Stack<Double> vals = new Stack<Double>();
            for (int i = 0; i < tokens.length; i++) {
                String t = tokens[i];
                if ("(".equals(t) || "".equals(t)) {
                    continue;
                }
                if ("+".equals(t) || "-".equals(t) || "*".equals(t) || "/".equals(t)) {
                    ops.push(t);
                } else if (")".equals(t)) {
                    applyTopOperator(ops, vals);
                } else {
                    vals.push(Double.valueOf(t));
                }
            }
            while (!ops.isEmpty()) {
                applyTopOperator(ops, vals);
            }
            return vals.pop();
        }

        private static void applyTopOperator(Stack<String> ops, Stack<Double> vals) {
            String op = ops.pop();
            double right = vals.pop();
            double left = vals.pop();
            if ("+".equals(op)) {
                vals.push(left + right);
            } else if ("-".equals(op)) {
                vals.push(left - right);
            } else if ("*".equals(op)) {
                vals.push(left * right);
            } else if ("/".equals(op)) {
                vals.push(left / right);
            }
        }
    }
}