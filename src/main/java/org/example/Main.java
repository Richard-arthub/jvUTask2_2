package org.example;
import org.example.ExpressionCalculator;

public class Main {
    public static void main(String[] args) {
        ExpressionCalculator F1 = new ExpressionCalculator();
        F1.setExpression("5*7+8-25");
        System.out.println(F1.calculate());
    }
}