package cz.muni.fi.pb162.hw02.impl;

import cz.muni.fi.pb162.hw02.LabelMatcher;
import cz.muni.fi.pb162.hw02.error.InvalidExpressionException;
import cz.muni.fi.pb162.hw02.LabelFilter;

/**
 * Factory for matcher production
 */
public final class LabeledOperations {

    private LabeledOperations() {
        // intentionally private
    }

    /**
     * Predicate evaluates if the expression contains only letters from alphabet,
     * or |, & or a whitespace.
     * @param expression to be checked
     * @return false if input contains invalid characters, else return true.
     */
    private static boolean validateAlphabetic(String expression){
        char[] array = expression.replaceAll(" ", "").toCharArray();
        for (char c : array){
            if(c != '!' && c != '&' && c != '|' && !(c >= 'a' && c <= 'z')
                    && !(c >= 'A' && c <= 'Z')){
                return false;
            }
        }
        return true;
    }

    /**
     * Predicate evaluates if the expression contains wrong negations.
     * @param expression to be checked
     * @return true if negations are written correctly, else false
     */
    private static boolean validateNegs(String expression){
        char[] array = expression.replaceAll(" ", "").toCharArray();
        for (int i = 0; i < array.length; i++){
            if (array[i] == '!' && i == 0){
                continue;
            }
            if (array[i] == '!' && (array[i - 1] != '&' && array[i - 1] != '|' && array[i - 1] != '!')){
                return false;
            }
        }
        return true;
    }

    /**
     * Predicate evaluates if the operator are used correctly.
     * @param expression to be checked
     * @return false if operators are used incorrectly, else return true.
     */
    private static boolean validateArgs(String expression){
        char[] array2 = expression.replaceAll(" ", "").replaceAll("!", "").toCharArray();
        for (int j = 0; j < array2.length; j++){
            if ((j == 0 || j == array2.length - 1) && (array2[j] == '&' || array2[j] == '|')){
                return false;
            }
        }
        return true;
    }

    /**
     * Predicate uses all predicates above to validate input expression
     * @param expression to be checked
     * @return true if input is invalid, else false
     */
    private static boolean validateInput(String expression){
        if (!validateAlphabetic(expression)){
            return true;
        }
        if (expression.isBlank()) {
            return true;
        }
        if (!validateNegs(expression)){
            return true;
        }
        return !validateArgs(expression);
    }
    /**
     * Produces instance of your {@link LabelMatcher} implementation
     * which matches based on given expression.
     *
     * @param expression expression for which the matcher is created
     * @throws InvalidExpressionException if expression is not valid
     * @return expression-based label matcher
     */
    public static LabelMatcher expressionMatcher(String expression) {

        if (validateInput(expression)){
            throw new InvalidExpressionException(expression);
        }
        return new Matcher(expression);
    }

    /**
     * Produces instance of your {@link LabelFilter} implementation
     * which filters based on given expression.
     *
     * @param expression expression for which the filter is created
     * @throws InvalidExpressionException if expression is not valid
     * @return expression-based label filter
     */
    public static LabelFilter expressionFilter(String expression) {
        if (validateInput(expression)){
            throw new InvalidExpressionException(expression);
        }
        return new Filter(expression);
    }

}
