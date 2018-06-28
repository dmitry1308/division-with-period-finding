import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the dividend:");
        int dividend = scanner.nextInt();
        System.out.println("Enter the divider:");
        int divider = scanner.nextInt();
        System.out.println("\n" + "Answer:" + "\n");

        Division divisionOperation = new Division();
        List<Stage> resultOfDivision = divisionOperation.calculate(dividend, divider);

        Formatter formatter = new DivisionFormatter();
        String finalResult = formatter.format(resultOfDivision, divisionOperation.getFinalQuotient(), dividend, divider);
        System.out.println(finalResult);
    }
}
