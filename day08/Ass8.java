import java.util.List;
import java.util.*;

public class Ass8 {

    public static void main(String[] args) {
        int[] array;
        try (Scanner sc  = new Scanner(System.in)) {
            String line = sc.nextLine();
            String[] split = line.split("(?!^)");

            array = new int[split.length];
            for (int i = 0; i < split.length; i++) {
                array[i] = Integer.valueOf(split[i]);
            }

            sc.close();
        }

        int width = 25;
        int height = 6;

        List<Wrapper> wrappers = new ArrayList<>();

        int[][] currentArray = new int[height][width];
        int widthInd = 0;
        int heightInd = 0;
        for (int i = 0; i < array.length; i++) {
            int val = array[i];

            currentArray[heightInd][widthInd] = val;

            widthInd++;
            widthInd = widthInd % width;

            if (widthInd == 0) {
                heightInd++;
            }

            if (widthInd == 0 && heightInd == height) {
                wrappers.add(new Wrapper(currentArray));
                currentArray = new int[height][width];

                widthInd = 0;
                heightInd = 0;
            }
        }

        wrappers.add(new Wrapper(currentArray));

        firstPart(wrappers);
    }

    private static void firstPart(List<Wrapper> wrappers) {
        int minZeros = wrappers.get(0).getZeroes();
        int index = 0;

        for (int i = 1; i < wrappers.size(); i++) {
            int zeros = wrappers.get(i).getZeroes();
            if (zeros < minZeros) {
                minZeros = zeros;
                index = i;
            }
        }
        
        System.out.println("FirstPart: " + wrappers.get(index).getOnes() * wrappers.get(index).getTwos());
        secondPart(wrappers);
    }

    private static void secondPart(List<Wrapper> wrappers) {
        int width = 25;
        int height = 6;

        int layersCount = wrappers.size();

        int[][] finalArray = new int[height][width];

        for (int i = 0; i < finalArray.length; i++) {
            for (int j = 0; j < finalArray[0].length; j++) {

                for (int z = 0; z < layersCount; z++) {
                    int val = wrappers.get(z).getValue(i, j);
                    if (val == 0 || val == 1) {
                        finalArray[i][j] = val;
                        System.out.print(val);
                        break;
                    }
                }
            }
            System.out.print("\n");
        }
    }

    private static class Wrapper {
        private final int[][] array;

        public Wrapper (int[][] array) {
            this.array = array;
        }

        public int getZeroes() {
            return countFor(0);
        }

        public int getOnes() {
            return countFor(1);
        }

        public int getTwos() {
            return countFor(2);
        }
        
        public int getValue(int i, int j) {
            return array[i][j];
        }

        private int countFor(int target) {
            int count = 0;

            for (int i = 0; i < array.length; i++) {
                for (int j = 0; j < array[i].length; j++) {
                    if (array[i][j] == target) {
                        count++;
                    }
                }
            }

            return count;
        }
    } 
}