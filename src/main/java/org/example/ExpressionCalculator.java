package org.example;

import org.example.InvalidExpressionException;
import org.example.stack;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Класс ExpressionCalculator для вычисления значения математического выражения, записанного в виде символьной строки
 * Класс поддерживает скобки, операнды {*, /, +, -}, целые неотрицательные числа (результат вычисления может быть нецелым)
 * Перед вычислением выражения необходимо его задать с помощью специального метода
 */
public class ExpressionCalculator {

    private String expression;
    /**
     * Метод позволяет задать новое выражение
     *
     * @param _expression математическое выражение в виде символьной строки
     */
    public void setExpression(String _expression) {
        expression = _expression;
        if (!isExpressionValid()) throw new InvalidExpressionException("Некорректный ввод выражения.");
    }
    /**
     * Метод позволяет получить текущее выражение
     *
     * @return символьная строка текущего выражения
     */
    public String getExpression() {return expression;}

    private boolean isExpressionValid() {
        // скобка не имеет пары
        // не цифра и не оператор
        // оператор в начале, проверить отдельно
        // вокруг оператора есть цифры
        // скобки действительно содержат числа и операнды внутри
        //
        char [] arrayExpression = expression.toCharArray();

        int bracketsCount = 0;
        for (char c : arrayExpression)
            if (!(Character.isDigit(c) || operators.contains(c)))
                if (c == '(') bracketsCount++;
                else if (c == ')') bracketsCount--;
                else return false;
        if (bracketsCount != 0) return false;

        if (operators.contains(arrayExpression[0]) || operators.contains(arrayExpression[arrayExpression.length-1]))
            return false;
        for (int i = 2; i < arrayExpression.length-3; i++)
            if (operators.contains(arrayExpression[i]))
                if (!((Character.isDigit(arrayExpression[i-1]) || arrayExpression[i-1] == ')') && (Character.isDigit(arrayExpression[i+1]) || arrayExpression[i+1] == '(')))
                    return false;

        stack<Integer> operCount = new stack<>();
        operCount.push(0);
        for (char c : arrayExpression) {
            if (c == '(') operCount.push(0);
            if (operators.contains(c)) operCount.push(operCount.pop() + 1);
            if (c == ')')
                if (operCount.pop() == 0) return false;
        }

        return true;
    }

    private char [] putElemIntoArray (char c, int idx, char [] arr)
    {
        char [] newArr = new char [arr.length+1];
        if (idx > 0) System.arraycopy(arr, 0, newArr, 0, idx);
        newArr[idx] = c;
        if (arr.length - idx > 0) System.arraycopy(arr, idx, newArr, idx+1, arr.length - idx);
        //System.out.println(newArr);
        return newArr;
    }

    private void modify() { //добавить скобки вокруг всех выражений, определяя верный порядок
        char [] arrayExpression = expression.toCharArray();
        char [] opersArray = {'*', '/', '+', '-'};

        //отдельно проверка и добавление скобок в начало-конец
        if (arrayExpression[0] != '(')
        {
            arrayExpression = putElemIntoArray('(', 0, arrayExpression);
            arrayExpression = putElemIntoArray(')', arrayExpression.length, arrayExpression);
        }
        else
        {
            boolean areMainBrackets = true;
            int check = 0;
            for (int i = 1; i < arrayExpression.length - 1; i++)
            {
                if (arrayExpression[i] == '(') check++;
                else if (arrayExpression[i] == ')') check--;
                if (check < 0) {
                    areMainBrackets = false;
                    break;
                }
            }
            if (!areMainBrackets)
            {
                arrayExpression = putElemIntoArray('(', 0, arrayExpression);
                arrayExpression = putElemIntoArray(')', arrayExpression.length, arrayExpression);
            }
        }

        int j = 0;
        stack<Integer> countOpersInBrackets = new stack<>();
        stack<Integer> openBracketIndex = new stack<>();
        stack<Integer> closeBracketIndex = new stack<>();
        while (j < arrayExpression.length)
        {
            if (arrayExpression[j] == '(') {
                countOpersInBrackets.push(0);
                openBracketIndex.push(j);
            }
            if (operators.contains(arrayExpression[j]))
                countOpersInBrackets.push(countOpersInBrackets.pop() + 1);

            if (arrayExpression[j] == ')')
            {
                //System.out.println(countOpersInBrackets.peek());
                closeBracketIndex.push(j);
                if (countOpersInBrackets.peek() != 1)
                {
                    for (int operIter = 0; operIter < 4; operIter++)
                    {
                        int l = openBracketIndex.peek();
                        while (l < closeBracketIndex.peek())
                        {
                            if (arrayExpression[l] == opersArray[operIter])
                            {
                                if (countOpersInBrackets.peek() != 1)
                                {
                                    if (Character.isDigit(arrayExpression[l - 1])) {
                                        int i = l - 1;
                                        while (Character.isDigit(arrayExpression[i])) i--;
                                        arrayExpression = putElemIntoArray('(', i + 1, arrayExpression);
                                        closeBracketIndex.push(closeBracketIndex.pop() + 1);
                                        l++;
                                        j++;
                                    } else {
                                        int i = l - 1;
                                        int bracketsPair = 1;
                                        while (bracketsPair > 0 && i > openBracketIndex.peek()) {
                                            if (arrayExpression[i] == '(') bracketsPair--;
                                            if (arrayExpression[i] == ')') bracketsPair++;
                                            i--;
                                        }
                                        arrayExpression = putElemIntoArray('(', i, arrayExpression);
                                        closeBracketIndex.push(closeBracketIndex.pop() + 1);
                                        l++;
                                        j++;
                                    }

                                    if (Character.isDigit(arrayExpression[l + 1]))
                                    {
                                        int i = l + 1;
                                        while (Character.isDigit(arrayExpression[i])) i++;
                                        arrayExpression = putElemIntoArray(')', i, arrayExpression);
                                        closeBracketIndex.push(closeBracketIndex.pop() + 1);
                                    }
                                    else
                                    {
                                        int i = l + 2;
                                        int bracketsPair = 1;
                                        while (bracketsPair > 0 && i < closeBracketIndex.peek())
                                        {
                                            if (arrayExpression[i] == '(') bracketsPair++;
                                            if (arrayExpression[i] == ')') bracketsPair--;
                                            i++;
                                        }
                                        arrayExpression = putElemIntoArray(')', i, arrayExpression);
                                        closeBracketIndex.push(closeBracketIndex.pop() + 1);
                                    }

                                    countOpersInBrackets.push(countOpersInBrackets.peek() - 1);
                                    l++;
                                    j++;
                                }
                                //else countOpersInBrackets.pop();
                                else l++;
                            }
                            else l++;
                        }
                    }
                }
                countOpersInBrackets.pop();
                openBracketIndex.pop();
                closeBracketIndex.pop();
            }
            j++;
        }


        expression = String.valueOf(arrayExpression);
    }


    private double performCalculation(double num2, double num1, char operator) {
        // подсчёт через case
        switch (operator)
        {
            case '*': return num1*num2;
            case '/': return num1/num2;
            case '+': return num1+num2;
            case '-': return num1-num2;
        }
        return 0.;
    }


    static final private List<Character> operators = Arrays.asList('*', '/', '+', '-');

    /**
     * Метод позволяет получить посчитанное значение текущего выражение
     *
     * @return посчитанное значение выражения в формате double
     */
    public double calculate() {

        stack<Double> numStack = new stack<>();
        stack<Character> symbStack = new stack<>();

        modify();

        char [] charExpr = expression.toCharArray();
        char currChar, prevChar;

        symbStack.push('(');//первый символ всегда открывающая скобка
        for (int i = 1; i < expression.length(); i ++)
        {
            currChar = charExpr[i];
            prevChar = charExpr[i-1];
            if (Character.isDigit(currChar) && Character.isDigit(prevChar)) numStack.push(10. * numStack.pop() + (double)(currChar - '0'));
            else if (Character.isDigit(currChar)) numStack.push((double)(currChar - '0'));
            else if (operators.contains(currChar) || currChar == '(') symbStack.push(currChar);
            else // тогда это закрывающая скобка
            {
                numStack.push(performCalculation(numStack.pop(), numStack.pop(), symbStack.pop()));
                symbStack.pop();
            }
        }

        return numStack.peek();
    }
}
