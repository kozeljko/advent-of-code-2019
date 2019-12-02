import java.util.Arrays;
import java.util.Scanner;

public class Ass2 {

    public static final int PART_TWO_CONSTANT = 19690720;

    public static void main(String[] args) {
        int[] array;
        try (Scanner sc  = new Scanner(System.in)) {
            String line = sc.nextLine();
            String[] split = line.split(",");

            array = new int[split.length];
            for (int i = 0; i < split.length; i++) {
                array[i] = Integer.valueOf(split[i]);
            }
        }

        if (args.length > 0 && args[0].equals("1")) {
            array[1] = 12;
            array[2] = 2;

            int result = partOne(array);
            System.out.println(result);
        } else if (args.length > 0 && args[0].equals("2")) {
            int result = partTwo(array);
            System.out.println(result);
        } else {
            System.out.println("No input");
        }
    }

    private static int partOne(int[] input) {
        int i = 0;
        while (i < input.length) {
            //System.out.println(printArray(input));

            int value = input[i];

            switch(value) {
                case 1:
                    int index1 = input[i + 1];
                    int index2 = input[i + 2];
                    int index3 = input[i + 3];

                    input[index3] = input[index1] + input[index2];
                    i += 4;
                    break;
                case 2:
                    int index12 = input[i + 1];
                    int index22 = input[i + 2];
                    int index32 = input[i + 3];

                    input[index32] = input[index12] * input[index22];
                    i += 4;
                    break;
                case 99:
                    i = input.length;
                    break;
            }
        }

        return input[0];
    }

    private static int partTwo(int[] input) {
        int[] tmp;

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                tmp = input.clone();
                
                tmp[1] = i;
                tmp[2] = j;

                if (partOne(tmp) == PART_TWO_CONSTANT) {
                    return 100 * i + j;
                }
            }
        } 

        return -1;
    }

    private static String printArray(int[] array) {
        String res = "==============================================\n";
        
        for (int i = 0; i < array.length; i++) {
            res += array[i] + " ";
        }

        return res;
    }
}