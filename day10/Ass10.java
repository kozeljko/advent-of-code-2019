import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class Ass10 {

    public static void main(String[] args) {
        List<String> linesToParse = new ArrayList<>();

        try (Scanner sc  = new Scanner(System.in)) {
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                linesToParse.add(line);
            }
            sc.close();
        }

        int[][] array = new int[linesToParse.size()][];
        for (int x = 0; x < linesToParse.size(); x++) {
            String line = linesToParse.get(x);

            String[] split = line.split("(?!^)");
    
            array[x] = new int[split.length];
            for (int j = 0; j < split.length; j++) {
                array[x][j] = split[j].equals(".") ? 0 : 1;
            }
        }

        Coordinates coords = doPartOne(array);
        doPartTwo(array, coords);
    }

    private static Coordinates doPartOne(int[][] array) {
        int maxScore = Integer.MIN_VALUE;
        int iIndexMax = 0;
        int jIndexMax = 0;


        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                int value = array[i][j];
                if (value == 0) {
                    continue;
                }

                //System.out.println("Trying " + i + " " + j);
                int indexValue = doStuff(array, i, j);
                if (indexValue > maxScore) {
                    // System.out.println(indexValue);
                    iIndexMax = i;
                    jIndexMax = j;
                    maxScore = indexValue;
                }
            }

        }

        System.out.println("Coords: (" + jIndexMax + "," + iIndexMax + ")");
        System.out.println("Boost: " + maxScore);

        return new Coordinates(jIndexMax, iIndexMax);
    }

    private static void doPartTwo(int[][] array, Coordinates coords) {
        Map<Double, List<Wrapper>> map = new HashMap<>();

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (j == coords.getX() && i == coords.getY()) {
                    continue;
                }

                if (array[i][j] == 0) {
                    continue;
                }

                int jDifference = coords.getX() - j;
                int iDifference = coords.getY() - i;

                double angle = Math.atan2(jDifference, iDifference);
                double distance = calcDistance(j, i, coords);

                List<Wrapper> wrappers = map.computeIfAbsent(angle, o -> new ArrayList<>());
                wrappers.add(new Wrapper(j, i, angle, distance));
            }
        }

        List<Double> negativeAngles = map.keySet().stream().filter(o -> o <= 0).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        List<Double> positiveAngles = map.keySet().stream().filter(o -> o > 0).sorted(Comparator.reverseOrder()).collect(Collectors.toList());

        List<Double> angles = new ArrayList<>();
        angles.addAll(negativeAngles);
        angles.addAll(positiveAngles);

        int count = 0;
        int index = 0;
        int targetDestruction = 200;
        while (true) {
            List<Wrapper> wrappers = map.get(angles.get(index));
            if (!wrappers.isEmpty()) {
                Wrapper wrapper = wrappers.remove(0);
                count++;

                if (count == targetDestruction) {
                    System.out.println("200th: " + wrapper.getX() + "," + wrapper.getY());
                    System.out.println("Boost: " + (wrapper.getX() * 100 +  wrapper.getY()));
                    break;
                }
            }

            index++;
            index = index % angles.size();
        }
    }

    private static double calcDistance(int x, int y, Coordinates coordinates) {
        return Math.sqrt(Math.pow((double)(coordinates.getX() - x), 2) + Math.pow((double)(coordinates.getY() - y), 2));
    }

    private static int doStuff(int[][] original, int iStart, int jStart) {
        int[][] array = cloneArray(original);

        // 0 - no asteroid
        // 1 - unvisited asteroid
        // 2 - visited asteroid

        int observedCount = 0;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (iStart == i && jStart == j) {
                    continue;
                }

                int value = array[i][j];
                if (value == 0 || value == 2) {
                    continue;
                }

                // System.out.println("=====");
                // System.out.println("Found: " + j + "," + i);

                int iDifference = i - iStart;
                int jDifference = j - jStart;

                // System.out.println("Difference: " + jDifference + "," + iDifference);

                int gcd = findGcd(iDifference, jDifference);
                // System.out.println("gcd + " + gcd);

                int iEle = iDifference / gcd;
                int jEle = jDifference / gcd;

                // System.out.println("iELe + " + iEle);
                // System.out.println("jEle + " + jEle);

                boolean found = false;
                int modifier = 1;
                while (true) {
                    int iTarget = iStart + iEle * modifier;
                    int jTarget = jStart + jEle * modifier;

                    // System.out.println("TryingIn: " +jTarget + " " + iTarget);

                    if (!withinBounds(iTarget, jTarget, array)) {
                        break;
                    }
                    // System.out.println("do");

                    int visitedNode = array[iTarget][jTarget];
                    if (visitedNode == 1) {
                        if (!found) {
                            observedCount++;
                            found = true;
                        }

                        // visit him
                        // System.out.println("2");
                        array[iTarget][jTarget] = 2;
                    }

                    modifier++;
                }
            }
        }

        // printArray(array);
        return observedCount;
    }

    private static boolean withinBounds(int iTarget, int jTarget, int[][] array) {
        if (iTarget < 0 || iTarget >= array.length) {
            // System.out.println("i");
            return false;
        }

        if (jTarget < 0 || jTarget >= array[0].length) {
            // System.out.println("j");
            return false;
        }

        return true;
    }

    private static int findGcd(int i, int j) {
        int absI = Math.abs(i);
        int absJ = Math.abs(j);

        if (absI == 0) {
            return absJ;
        }

        if (absJ == 0) {
            return absI;
        }

        return gcdByEuclidsAlgorithm(absI, absJ);
    }

    private static int gcdByEuclidsAlgorithm(int n1, int n2) {
        if (n2 == 0) {
            return n1;
        }
        return gcdByEuclidsAlgorithm(n2, n1 % n2);
    }

    private static void printArray(int[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                System.out.print(array[i][j]);
            }
            System.out.println("");
        }
    }

    private static int[][] cloneArray(int[][] array) {
        int[][] cloneArray = new int[array.length][];
        for (int i = 0; i < array.length; i++) {
            cloneArray[i] = new int[array[i].length];
            for (int j = 0; j < array[i].length; j++) {
                cloneArray[i][j] = array[i][j];
            }
        }

        return cloneArray;
    }

    private static class Coordinates {
        private int x;
        private int y;

        public Coordinates(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    private static class Wrapper {
        private int x;
        private int y;
        private double angle;
        private double distance;

        public Wrapper(int x, int y, double angle, double distance) {
            this.x = x;
            this.y = y;
            this.angle = angle;
            this.distance = distance;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public double getAngle() {
            return angle;
        }

        public double getDistance() {
            return distance;
        }
    }
}