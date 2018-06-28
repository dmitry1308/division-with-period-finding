import java.util.ArrayList;
import java.util.List;

public class Division implements MathOperation {
    private List<Stage> stages = new ArrayList<Stage>();
    private String finalQuotient = "";

    @Override
    public List<Stage> calculate(int dividend, int divider) throws IllegalArgumentException {
        if (divider == 0) {
            throw new IllegalArgumentException("divider cannot be 0, division by zero");
        }
        if (dividend == 0) {
            stages.add(new Stage(dividend, divider));
            return stages;
        }

        if (divider < 0 && dividend < 0) {
            dividend = Math.abs(dividend);
            divider = Math.abs(divider);
        } else if (dividend < 0) {
            finalQuotient += "-";
            dividend = Math.abs(dividend);
        } else if (divider < 0) {
            finalQuotient += "-";
            divider = Math.abs(divider);
        }

        if (dividend < divider) {
            finalQuotient += "0.";
            Integer incompleteQuotient;
            incompleteQuotient = dividend * 10;
            calculatePeriod(incompleteQuotient, divider);
            return stages;
        } else {
            calculateWhenDividendMoreDividerWithPeriod(dividend, divider);
        }
        return stages;
    }


    private void calculateWhenDividendMoreDividerWithPeriod(int dividend, int divider) {
        int incompleteQuotient;
        int balance = 0;
        String[] digits = String.valueOf(dividend).split("");
        StringBuilder variableForInCompletePrivate = new StringBuilder();
        int dividerDigit = (int) Math.log10(divider) + 1;
        for (int numberOfStage = 0; numberOfStage < digits.length; numberOfStage++) {
            variableForInCompletePrivate.append(digits[numberOfStage]);
            incompleteQuotient = Integer.parseInt(variableForInCompletePrivate.toString());

            if (incompleteQuotient >= divider) {
                balance = calculateStage(incompleteQuotient, divider);
                variableForInCompletePrivate.replace(
                        0, variableForInCompletePrivate.length(),
                        String.valueOf(balance));
            } else {
                if (numberOfStage >= dividerDigit) {
                    finalQuotient += "0";
                }
            }
        }
        if (balance == 0) return;
        finalQuotient += ".";
        Stage lastStage = stages.get(stages.size() - 1);
        int finalBalance = lastStage.getIncompleteQuotient() - lastStage.getMultiplyResult();
        incompleteQuotient = finalBalance * 10;
        calculatePeriod(incompleteQuotient, divider);
    }

    private void calculatePeriod(Integer incompleteQuotient, int divider) {
        ArrayList<Integer> balances = new ArrayList<Integer>();
        int dividend = incompleteQuotient / 10;
        for (int i = 0; i < 10; i++) {
            if (incompleteQuotient > divider) {
                int balance = calculateStage(incompleteQuotient, divider);
                if (balance == 0 || balance == (dividend) || balance == (incompleteQuotient / 10) || balances.contains(balance))
                    break;
                balances.add(balance);
                incompleteQuotient = balance * 10;
            } else {
                finalQuotient += "0";
                incompleteQuotient *= 10;
            }
        }
    }

    private int calculateStage(Integer incompleteQuotient, int divider) {
        int multiplyResult = incompleteQuotient / divider * divider;
        Stage stage = new Stage(incompleteQuotient, multiplyResult);
        stages.add(stage);
        int quotient = incompleteQuotient / divider;
        finalQuotient += quotient;
        return incompleteQuotient % divider;
    }

    public String getFinalQuotient() {
        return finalQuotient;
    }
}

