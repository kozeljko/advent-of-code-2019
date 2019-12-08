import java.util.Arrays;
import java.util.Scanner;

public class Ass7 {

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

        int[] indexes = new int[5];
        int[] elements = new int[5];
        boolean isFirstPart = false;
        for (int i = 0; i < 5; i++) {
            indexes[i] = 0;
            elements[i] = isFirstPart ? i : i + 5;
        }

        int i = 0;
        int max = Integer.MIN_VALUE;
        // Do permutations
        while (i < 5) {
            if (indexes[i] < i) {
                swap(elements, i % 2 == 0 ?  0: indexes[i], i);
                //System.out.println(printArray(elements));
                int value = attemptThing(elements, array);
                if (value > max) {
                    max = value;
                }

                indexes[i]++;
                i = 0;
            }
            else {
                indexes[i] = 0;
                i++;
            }
        }

        System.out.println(max);
    }
    
    private static void swap(int[] input, int a, int b) {
        int tmp = input[a];
        input[a] = input[b];
        input[b] = tmp;
    }

    private static int attemptThing(int[] permute, int[] array) {
        int value = 0;

        Input[] doubleArray = new Input[5];
        for (int i = 0; i < 5; i++) {
            doubleArray[i] = new Input(Arrays.copyOf(array, array.length), 0);
        }

        boolean firstTime = true;
        int i = 0;
        while ( i < 5) {
            Input input = doubleArray[i];
            Output output = doThing(input, permute[i], value, firstTime);
            input.setIndex(output.getIndex());
            //System.out.println("Value: " + output.getValue());
            if (output.getWasHalted()) {
                return value;
            } else {
                value = output.getValue();
            }
            
            i++;
            if (i == 5) {
                firstTime = false;
            }
            i = i % 5;
        }

        return value;
    } 

    private static Output doThing(Input inputObj, int first, int second, boolean firstLoop) {
        boolean doFirst = true;
        int i = inputObj.getIndex();
        int[] input = inputObj.getArray();
        while (i < input.length) {
            OpCode opCode = OpCode.createOpCode(input[i]);


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

                    input[index13] = getInput(doFirst, first, second, firstLoop);
                    doFirst = false;
                    i+=2;
                    //System.out.println("Moved to:" + i);
                    //System.out.println(printArray(input));
                    break;
                case 4:
                    int value14 = opCode.isPositionModeAtParameter(1) ? input[input[i + 1]] : input[i + 1];
                    i+=2;
                    //System.out.println("Out:" + value14);
                    //System.out.println(i);
                    return new Output(false, value14, i);
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
                    //System.out.println("Halt:");
                    return new Output(true, 0, i);
                default:
                    System.out.println("Shouldn't happen");
                    i = input.length;
                    break;
            }
        }

        return null;
    }

    private static int getInput(boolean doFirst, int first, int second, boolean firstLoop) {
        if (firstLoop) {
            return doFirst ? first : second;
        } else {
            return second;
        }
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

    private static class Input {
        private final int[] array;
        private int index;

        public Input(int[] array, int index) {
            this.array = array;
            this.index = index;
        }

        public int[] getArray() {
            return array;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index)  {
            this.index = index;
        }
    } 

    private static class Output {
        private final boolean wasHalted;
        private final int value;
        private final int index;

        public Output(boolean wasHalted, int value, int index) {
            this.wasHalted = wasHalted;
            this.value = value;
            this.index = index;
        }

        public boolean getWasHalted() {
            return wasHalted;
        }

        public int getValue() {
            return value;
        }

        public int getIndex() {
            return index;
        }
    }
}