package org.example;
import org.example.ExpressionCalculator;

public class Main {
    public static void main(String[] args) {
        ExpressionCalculator F1 = new ExpressionCalculator();
        F1.setExpression("(2+3)*(5+3)");
        System.out.println(F1.calculate());
    }
}