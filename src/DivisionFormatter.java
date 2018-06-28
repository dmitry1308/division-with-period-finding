import java.util.ArrayList;
import java.util.List;

public class DivisionFormatter implements Formatter {
    private ArrayList<String> finalResult = new ArrayList<String>();

    @Override
    public String format(List<Stage> stages, String finalQuotient, int dividend, int divider) {
        if (stages.get(0).getIncompleteQuotient() == 0) {
            return stages.get(0).getIncompleteQuotient() + " / " + stages.get(0).getMultiplyResult() + " = 0";
        }

        Stage stage0 = stages.get(0);
        int shift = calculateDigit(stage0.getIncompleteQuotient()) + 1;
        addStage(shift, stage0);
        if (stages.size() == 1) {
            addFinalBalance(shift, stage0);
        }

        for (int i = 1; i < stages.size(); i++) {
            Stage stagePrevious = stages.get(i - 1);
            Stage stageCurrent = stages.get(i);
            shift += calculateLocalShift(stagePrevious, stageCurrent);
            addStage(shift, stageCurrent);

            if (i == stages.size() - 1) {
                addFinalBalance(shift, stageCurrent);
            }
        }

        String formattedFinalQuotient = formatFinalQuotient(finalQuotient, stages, dividend);

        changeFirstLine(dividend, divider, stages.get(0));
        changeSecondLine(dividend, formattedFinalQuotient);
        changeThirdLine(dividend, formattedFinalQuotient);

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < finalResult.size(); i++) {
            result.append(finalResult.get(i)).append("\n");
        }
        return result.toString();
    }

    private String formatFinalQuotient(String finalQuotient, List<Stage> stages, int dividend) {
        if (!finalQuotient.contains(".")) return finalQuotient;
        Stage lastStage = stages.get(stages.size() - 1);
        int lastBalance = lastStage.getIncompleteQuotient() - lastStage.getMultiplyResult();
        int indexOfComma = finalQuotient.indexOf(".");
        String finalQuotientBeforeComma = finalQuotient.substring(0, indexOfComma);
        String finalQuotientAfterComma = finalQuotient.substring(indexOfComma + 1);
        if ((lastBalance != 0) && (lastBalance == dividend)) {
            return String.format("%s.(%s)", finalQuotientBeforeComma, finalQuotientAfterComma);
        }
        if (finalQuotientAfterComma.length() == 10) {
            return finalQuotient;
        }

        if (lastBalance != 0) {
            int indexBeginningRepeatFromEnd = 0;
            for (int i = stages.size() - 1; i >= 0; i--) {
                if ((stages.get(i).getIncompleteQuotient() / 10) == lastBalance) {
                    indexBeginningRepeatFromEnd = stages.size() - 1 - i;
                    break;
                }
            }
            String finalQuotientReverse = new StringBuilder(finalQuotient).reverse().toString();
            String firstPart = finalQuotientReverse.substring(0, indexBeginningRepeatFromEnd + 1);
            String secondPart = finalQuotientReverse.substring(indexBeginningRepeatFromEnd + 1);
            String full = String.format(")%s(%s", firstPart, secondPart);
            return new StringBuilder(full).reverse().toString();
        }
        return finalQuotient;
    }

    private void addFinalBalance(int shift, Stage stageCurrent) {
        finalResult.add(String.format(
                "%" + shift + "s",
                stageCurrent.getIncompleteQuotient() - stageCurrent.getMultiplyResult()));
    }

    private int calculateLocalShift(Stage stagePrevious, Stage stageCurrent) {
        int numberSymbolsPreviousBalance = calculateDigit(
                stagePrevious.getIncompleteQuotient() - stagePrevious.getMultiplyResult());
        int numberSymbolsCurrentIncompleteQuotient = calculateDigit(stageCurrent.getIncompleteQuotient());
        return numberSymbolsCurrentIncompleteQuotient - numberSymbolsPreviousBalance;
    }

    private void addStage(int shift, Stage stage) {
        finalResult.add(createLineOfInCompletePrivate(shift, stage.getIncompleteQuotient()));
        finalResult.add(createLineOfMultiply(shift, stage.getMultiplyResult()));
        finalResult.add(createTrait(shift, stage.getIncompleteQuotient(), stage.getMultiplyResult()));
    }

    private String createLineOfInCompletePrivate(int shift, int incompleteQuotient) {
        return String.format("%" + shift + "s", "_" + incompleteQuotient);
    }

    private String createLineOfMultiply(int shift, int multiplyResult) {
        return String.format("%" + shift + "d", multiplyResult);
    }

    private String createTrait(int numberOfStage, int incompleteQuotient, int multiplyResult) {

        Integer tab = createLineOfInCompletePrivate(numberOfStage, incompleteQuotient).length() - calculateDigit(multiplyResult);
        return makeTrait(incompleteQuotient, tab);
    }

    private String makeTrait(Integer incompleteQuotient, Integer tab) {
        String numberOfSpaces = assemblyString(tab, ' ');
        String numberOfDashes = assemblyString(calculateDigit(incompleteQuotient), '-');
        return numberOfSpaces + numberOfDashes;
    }

    private void changeFirstLine(int dividend, int divider, Stage stage0) {
        int maxDividendOrMultiplyResult = Math.max(calculateDigit(Math.abs(dividend)), calculateDigit(stage0.getMultiplyResult()));
        int shift = calculateDigit(Math.max(maxDividendOrMultiplyResult, stage0.getIncompleteQuotient()));
        String line = String.format("_" + "%" + "-" + (shift) + "d", Math.abs(dividend)) + "|" + divider;
        finalResult.set(0, line);
    }

    private void changeSecondLine(int dividend, String finalQuotient) {
        String numberOfSpacesS = "";
        String beginningOfLine = finalResult.get(1);
        int numberDigitsInBeginningOfLine = beginningOfLine.length();
        int numberOfSpacesI = calculateDigit(Math.abs(dividend)) - numberDigitsInBeginningOfLine + 1;
        for (int j = 0; j < numberOfSpacesI; j++) {
            numberOfSpacesS += " ";
        }
        String numberOfLineUnderDivider = "";
        for (int j = 0; j < finalQuotient.length(); j++) {
            numberOfLineUnderDivider += "-";
        }
        String finalLine = beginningOfLine + numberOfSpacesS + "|" + numberOfLineUnderDivider;
        finalResult.set(1, finalLine);
    }

    private void changeThirdLine(int dividend, String finalQuotient) {
        String numberOfSpacesS = "";
        String beginningOfLine = finalResult.get(2);
        int numberLinesUnderNumberUnderInCompletePrivate = beginningOfLine.length();
        int numberOfSpacesI = calculateDigit(Math.abs(dividend)) - numberLinesUnderNumberUnderInCompletePrivate + 1;
        for (int j = 0; j < numberOfSpacesI; j++) {
            numberOfSpacesS += " ";
        }

        String finalLine = beginningOfLine + numberOfSpacesS + "|" + finalQuotient;
        finalResult.set(2, finalLine);
    }

    private int calculateDigit(int i) {
        i = Math.abs(i);
        if (i == 0) {
            return 0;
        }
        return (int) Math.log10(i) + 1;
    }

    private String assemblyString(int numberOfSymbols, char symbol) {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < numberOfSymbols; i++) {
            string.append(symbol);
        }
        return string.toString();
    }

    public String getCreateLineOfInCompletePrivate(int numberOfStage, int incompleteQuotient) {
        return createLineOfInCompletePrivate(numberOfStage, incompleteQuotient);
    }

    public String getCreateLineOfMultiply(int numberOfStage, int multiplyResult) {
        return createLineOfMultiply(numberOfStage, multiplyResult);
    }

    public String getCreateTrait(int numberOfStage, int incompleteQuotient, int multiplyResult) {
        return createTrait(numberOfStage, incompleteQuotient, multiplyResult);
    }

    public String getMakeTrait(Integer incompleteQuotient, Integer tab) {
        return makeTrait(incompleteQuotient, tab);
    }

    public String getAssemblyString(int numberOfSymbols, char symbol) {
        return assemblyString(numberOfSymbols, symbol);
    }

    public String getFormatFinalQuotient(String finalQuotient, List<Stage> stages, int dividend) {
        return formatFinalQuotient(finalQuotient, stages, dividend);
    }

    public int getCalculateLocalShift(Stage stagePrevious, Stage stageCurrent) {
        return calculateLocalShift(stagePrevious, stageCurrent);
    }

}