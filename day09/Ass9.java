import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Ass9 {

    public static final int PART_TWO_CONSTANT = 19690720;

    public static void main(String[] args) {
        long[] array;
        try (Scanner sc  = new Scanner(System.in)) {
            String line = sc.nextLine();
            String[] split = line.split(",");

            array = new long[split.length];
            for (int i = 0; i < split.length; i++) {
                array[i] = Long.valueOf(split[i]);
            }

            sc.close();
        }

        doThing(new Memory(array), getInput());
    }

    private static void doThing(Memory memory, InputObject inputObject) {
        while (memory.canRun()) {
            switch(memory.getOpCode()) {
                case 1:
                    long sum = memory.getFirstParameter() + memory.getSecondParameter();

                    memory.writeValue(memory.getWriteAddress(3), sum);
                    memory.movePointer(4);
                    break;
                case 2:
                    long product = memory.getFirstParameter() * memory.getSecondParameter();

                    memory.writeValue(memory.getWriteAddress(3), product);
                    memory.movePointer(4);
                    break;
                case 3:
                    memory.writeValue(memory.getWriteAddress(1), inputObject.getValue());
                    memory.movePointer(2);
                    break;
                case 4:
                    long readValue = memory.getFirstParameter();
                    memory.movePointer(2);

                    System.out.println(readValue);
                    break;
                case 5:
                    if (memory.getFirstParameter() != 0) {
                        memory.jump(memory.getSecondParameter());
                    } else {
                        memory.movePointer(3);
                    }

                    break;
                case 6:
                    if (memory.getFirstParameter() == 0) {
                        memory.jump(memory.getSecondParameter());
                    } else {
                        memory.movePointer(3);
                    }
                    break;        
                case 7:
                    memory.writeValue(memory.getWriteAddress(3), memory.getFirstParameter() < memory.getSecondParameter() ? 1 : 0);
                    memory.movePointer(4);
                    break;
                case 8:
                    memory.writeValue(memory.getWriteAddress(3), memory.getFirstParameter() == memory.getSecondParameter() ? 1 : 0);
                    memory.movePointer(4);
                    break;
                case 9:
                    memory.adjustRelativeBase(memory.getFirstParameter());
                    memory.movePointer(2);
                    break;
                case 99:
                    return;
                default:
                    System.out.println("Shouldn't happen");
                    return;
            }
        }
    }

    private static InputObject getInput() {
        // Set by hand
        return new InputObject(2);
    }

    private static String printArray(int[] array) {
        String res = "==============================================\n";
        
        for (int i = 0; i < array.length; i++) {
            res += array[i] + " ";
        }

        return res;
    }

    private static class OpCodeObject {
        private String operation;

        public OpCodeObject(long input) {
            String string = String.valueOf(input);
            while (string.length() < 5) {
                string = "0" + string;
            }

            operation = string;
        }

        public int getOpCode() {
            return Integer.valueOf(operation.substring(operation.length() - 2));
        }

        public boolean isPositionModeAtParameter(int index) {
            return operation.charAt(operation.length() - 2 - index) == '0';
        }

        public boolean isImmediateModeAtParameter(int index) {
            return operation.charAt(operation.length() - 2 - index) == '1';
        }

        public boolean isRelativeModeAtParameter(int index) {
            return operation.charAt(operation.length() - 2 - index) == '2';
        }
    }

    private static class Memory {
        private long[] array;
        private int index;
        private int relativeBase;
        private OpCodeObject currentOpcode;

        public Memory(long[] array) {
            this.array = array;
            index = 0;
            relativeBase = 0;
            setOpCode();
        }

        private void adjustMemory(int address) {
            int currentSize = array.length;
            int factor = 5;
            
            int newSize = (address - (currentSize - 1)) * factor;
            array = Arrays.copyOf(array, currentSize + newSize);
        }

        public boolean canRun() {
            return true;
        }

        public int getOpCode() {
            return currentOpcode.getOpCode();
        }

        private void setOpCode() {
            currentOpcode = new OpCodeObject(readAddress(index));
        }

        private long readAddress(int address) {
            if (address >= array.length) {
                adjustMemory(address);
            }

            return array[address];
        }

        public void writeValue(int address, long value) {
            if (address >= array.length) {
                adjustMemory(address);
            }

            array[address] = value;
        }

        private long getNthParameter(int nth, boolean writeAddress) {
            if (writeAddress) {
                if (currentOpcode.isRelativeModeAtParameter(nth)) {
                    return readAddress(index + nth) + relativeBase;
                } else { // positional
                    return readAddress(index + nth);
                }
            }

            if (currentOpcode.isPositionModeAtParameter(nth)) {
                return readAddress((int)readAddress(index + nth));
            }

            if (currentOpcode.isImmediateModeAtParameter(nth)) {
                return readAddress(index + nth);
            }

            if (currentOpcode.isRelativeModeAtParameter(nth)) {
                return readAddress((int)readAddress(index + nth) + relativeBase);
            }

            return -1;
        }

        public void movePointer(int step) {
            index = index + step;
            setOpCode();
        }

        public void jump(long address) {
            this.index = (int)address;
            setOpCode();
        }

        public void adjustRelativeBase(long adjustment) {
            relativeBase = relativeBase + (int)adjustment;  
        }

        public int getWriteAddress(int nth) {
            return (int)getNthParameter(nth, true);
        }

        public long getFirstParameter() {
            return getNthParameter(1, false);
        }

        public long getSecondParameter() {
            return getNthParameter(2, false);
        }
    }

    private static class InputObject {
        private final Queue<Long> queue = new LinkedList<>();
        private final long defaultValue;

        public InputObject(long defaultValue) {
            this.defaultValue = defaultValue;
        }

        public long getValue() {
            if (queue.isEmpty()) {
                return defaultValue;
            }

            return queue.remove();
        }

        public void addValue(long value) {
            queue.add(value);
        }
    }
}