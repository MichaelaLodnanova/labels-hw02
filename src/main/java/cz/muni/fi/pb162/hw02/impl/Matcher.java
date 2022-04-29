package cz.muni.fi.pb162.hw02.impl;

import cz.muni.fi.pb162.hw02.HasLabels;
import cz.muni.fi.pb162.hw02.LabelMatcher;
import cz.muni.fi.pb162.hw02.impl.utils.StringUtils;


/**
 * Class Matcher represents an object created of a statement to be evaluated
 * @author Michaela Lodnanova
 */
public class Matcher implements LabelMatcher {

    private static final char AND = '&';
    private static final char OR = '|';

    private final String matchingExpression;


    /**
     * Constructor of Matcher
     * Represents an object with a matching expression to be evaluated
     * @param matchingExpression Expression that matcher will use to match with requested set of labels
     */
    public Matcher(String matchingExpression) {
        this.matchingExpression = matchingExpression;
    }

    @Override
    public boolean matches(HasLabels labeled) {

        boolean result;
        String expressionToParse = matchingExpression;
        expressionToParse = expressionToParse.replaceAll(" ", "");
        //get first expression
        String expression = getExpression(expressionToParse);
        if (expression.length() <= 0) {
            //expression hasn't found
            return false;
        }
        //evaluate expression against set of labels
        boolean firstOperand = evaluateExpression(expression, labeled);
        //update result
        result = firstOperand;
        //update parsed expression (remove expression from the beginning)
        expressionToParse = expressionToParse.substring(expression.length());
        boolean hasNextOperand = false;
        char operator = AND;
        if (expressionToParse.length() > 0) {
            //get first operator
            operator = expressionToParse.charAt(0);
            //update parsed expression (remove operator from the beginning)
            expressionToParse = expressionToParse.substring(1);
            hasNextOperand = true;
        }

        boolean secondOperand;
        while (hasNextOperand) {
            expression = getExpression(expressionToParse);
            secondOperand = evaluateExpression(expression, labeled);
            result = evaluateExpression(firstOperand, secondOperand, operator);
            //get next operator
            expressionToParse = expressionToParse.substring(expression.length());
            if (expressionToParse.length() > 0) {
                //set first operand
                firstOperand = result;
                //get next operator
                operator = expressionToParse.charAt(0);
                expressionToParse = expressionToParse.substring(1);
            } else {
                hasNextOperand = false;
            }
        }
        return result;
    }

    /**
     * Evaluate logical expression (firstOperand operator secondOperand)
     * @param firstOperand represents boolean on left side
     * @param secondOperand represents boolean on right side
     * @param operator represents operators & and |
     * @return true if statement is true, else false
     */
    private boolean evaluateExpression(boolean firstOperand, boolean secondOperand, char operator) {
        switch (operator) {
            case AND:
                return firstOperand && secondOperand;
            case OR:
                return firstOperand || secondOperand;
            default: return false;
        }
    }

    /**
     * Evaluate whether the expression matches with a label within the set of
     * labeled parameter
     * @param expression to be evaluated
     * @param labeled of labeles
     * @return true if expression is true, else return false
     */
    private boolean evaluateExpression(String expression, HasLabels labeled) {
        int countOfNegation = StringUtils.countSubsequentChars(expression, '!', 0);
        expression = expression.substring(countOfNegation);
        for (String label : labeled.getLabels()) {
            if (expression.equals(label)) {
                return countOfNegation % 2 == 0;
            }
        }
        return countOfNegation % 2 != 0;
    }

    /**
     * Method which parses the expression and gets a substring from it
     * @param expressionToParse to be parsed
     * @return substring
     */
    private String getExpression(String expressionToParse) {
        int indexOfAND = expressionToParse.indexOf(AND);
        int indexOfOR = expressionToParse.indexOf(OR);

        if (Math.max(indexOfAND, indexOfOR) == -1) {
            return expressionToParse;
        }

        return (Math.min(indexOfAND, indexOfOR) == -1) ?
                expressionToParse.substring(0, Math.max(indexOfAND, indexOfOR)) :
                expressionToParse.substring(0, Math.min(indexOfAND, indexOfOR));
    }

    @Override
    public boolean all(Iterable<HasLabels> labeled) {
        for (HasLabels labels : labeled) {
            if (!matches(labels)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean any(Iterable<HasLabels> labeled) {
        for (HasLabels labels : labeled) {
            if (matches(labels)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean none(Iterable<HasLabels> labeled) {
        for (HasLabels labels : labeled) {
            if (matches(labels)) {
                return false;
            }
        }
        return true;
    }
}
