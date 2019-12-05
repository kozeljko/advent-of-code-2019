import java.util.Arrays;
import java.util.Scanner;

public class Ass5 {

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

            sc.close();
        }

        if (args.length > 0 && args[0].equals("1")) {
            partOne(array);
            //System.out.println(result);
        }  else {
            System.out.println("No input");
        }
    }

    private static void partOne(int[] input) {
        int i = 0;
        while (i < input.length) {
            //System.out.println(i);
            OpCode opCode = OpCode.createOpCode(input[i]);
/*
            System.out.println(printArray(input));
            System.out.println(opCode.getOpCode());
            System.out.println(opCode.isPositionModeAtParameter(1));
            System.out.println(opCode.isPositionModeAtParameter(2));
            System.out.println("=====");*/

            switch(opCode.getOpCode()) {
                case 1:
                    int value1 = opCode.isPositionModeAtParameter(1) ? input[input[i + 1]] : input[i + 1];
                    int value2 = opCode.isPositionModeAtParameter(2) ? input[input[i + 2]] : input[i + 2];
                    int index3 = input[i + 3];

                    input[index3] = value1 + value2;
                    i += 4;
                    break;
                case 2:
                    int value12 = opCode.isPositionModeAtParameter(1) ? input[input[i + 1]] : input[i + 1];
                    int value22 = opCode.isPositionModeAtParameter(2) ? input[input[i + 2]] : input[i + 2];
                    int index32 = input[i + 3];

                    input[index32] = value12 * value22;
                    i += 4;
                    break;
                case 3:
                    int index13 = input[i + 1];

                    input[index13] = getInput();
                    i += 2;
                    break;
                case 4:
                    int value14 = opCode.isPositionModeAtParameter(1) ? input[input[i + 1]] : input[i + 1];
                    System.out.println(value14);
                    i += 2;
                    break;
                case 5:
                    int value15 = opCode.isPositionModeAtParameter(1) ? input[input[i + 1]] : input[i + 1];
                    if (value15 != 0) {
                        i = opCode.isPositionModeAtParameter(2) ? input[input[i + 2]] : input[i + 2];
                    } else {
                        i += 3;
                    }
                    break;
                case 6:
                    int value16 = opCode.isPositionModeAtParameter(1) ? input[input[i + 1]] : input[i + 1];
                    if (value16 == 0) {
                        i = opCode.isPositionModeAtParameter(2) ? input[input[i + 2]] : input[i + 2];
                    } else {
                        i += 3;
                    }
                    break;        
                case 7:
                    int value17 = opCode.isPositionModeAtParameter(1) ? input[input[i + 1]] : input[i + 1];
                    int value27 = opCode.isPositionModeAtParameter(2) ? input[input[i + 2]] : input[i + 2];
                    int index37 = input[i + 3];

                    if (value17 < value27) {
                        input[index37] = 1;
                    } else {
                        input[index37] = 0;
                    }
                    i+=4;
                    break;
                case 8:
                    int value18 = opCode.isPositionModeAtParameter(1) ? input[input[i + 1]] : input[i + 1];
                    int value28 = opCode.isPositionModeAtParameter(2) ? input[input[i + 2]] : input[i + 2];
                    int index38 = input[i + 3];

                    if (value18 == value28) {
                        input[index38] = 1;
                    } else {
                        input[index38] = 0;
                    }
                    i+=4;
                    break;
                case 99:
                    i = input.length;
                    break;
                default:
                    System.out.println("Shouldn't happen");
                    i = input.length;
                    break;
            }
        }
    }

    private static int getInput() {
        return 5;
    }

    private static String printArray(int[] array) {
        String res = "==============================================\n";
        
        for (int i = 0; i < array.length; i++) {
            res += array[i] + " ";
        }

        return res;
    }

    private static class OpCode {
        private String operation;

        private OpCode(int input) {
            String string = String.valueOf(input);
            while (string.length() < 4) {
                string = "0" + string;
            }

            operation = string;
        }

        public static OpCode createOpCode(int input) {
            return new OpCode(input);
        }

        public int getOpCode() {
            return Integer.valueOf(operation.substring(operation.length() - 2));
        }

        public boolean isPositionModeAtParameter(int index) {
            return operation.charAt(operation.length() - 2 - index) == '0';
        }
    }
}