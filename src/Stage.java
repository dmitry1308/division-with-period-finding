public class Stage {
    private int incompleteQuotient;
    private int multiplyResult;

    public Stage(int incompleteQuotient, int multiplyResult) {
        this.incompleteQuotient = incompleteQuotient;
        this.multiplyResult = multiplyResult;
    }

    public int getIncompleteQuotient() {
        return incompleteQuotient;
    }

    public int getMultiplyResult() {
        return multiplyResult;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stage stage = (Stage) o;

        if (incompleteQuotient != stage.incompleteQuotient) return false;
        return multiplyResult == stage.multiplyResult;
    }

    @Override
    public int hashCode() {
        int result = incompleteQuotient;
        result = 31 * result + multiplyResult;
        return result;
    }
}

