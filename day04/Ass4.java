import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Ass4 {
    public static void main(String[] args) {
        int lowerLimit1 = 138241;
        int upperLimit2 = 674034;

        long count1 = IntStream.range(lowerLimit1, upperLimit2 + 1)
            .filter(Ass4::fitsNeighbourCriteria)
            .filter(Ass4::fitsNonDescendingCriteria)
            .count();

        System.out.println("First task: " + count1);

        long count2 = IntStream.range(lowerLimit1, upperLimit2 + 1)
            .filter(Ass4::fitsNeighbourCriteriaTwo)
            .filter(Ass4::fitsNonDescendingCriteria)
            .count();

        System.out.println("Second task: " + count2);
    }

    private static boolean fitsNeighbourCriteria(int number) {
        String string = String.valueOf(number);
        
        for (int i = 0; i < string.length() - 1; i++) {
            if (string.charAt(i) == string.charAt(i + 1)) {
                return true;
            }
        }

        return false;
    }

    private static boolean fitsNeighbourCriteriaTwo(int number) {
        String string = String.valueOf(number);

        char previousChar = string.charAt(0);
        int count = 1;
        for (int i = 1; i < string.length(); i++) {
            char newChar = string.charAt(i);
            if (newChar == previousChar) {
                count++;
            } else {
                if (count == 2) {
                    //System.out.println("Correct " + number);
                    return true;
                } else {
                    count = 1;
                    previousChar = newChar;
                }
            }
        }



        return count == 2;
    }

    private static boolean fitsNonDescendingCriteria(int number) {
        String string = String.valueOf(number);

        for (int i = 0; i < string.length() - 1; i++) {

            if (string.charAt(i) > string.charAt(i + 1)) {
                return false;
            }
        }

        return true;
    }
}