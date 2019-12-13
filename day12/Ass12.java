import java.util.Scanner;
import java.util.*;

public class Ass12 {

    public static void main(String[] args) {
        Moon[] moons = new Moon[4];
        int index = 0;
        try (Scanner sc = new Scanner(System.in)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                String cleaned = line.replace("<", "").replace(">", "").replace("=", "").replace("x", "")
                        .replace("y", "").replace("z", "").replace(" ", "");

                String[] split = cleaned.split(",");
                moons[index] = new Moon(index + 1, Integer.valueOf(split[0]), Integer.valueOf(split[1]),
                        Integer.valueOf(split[2]));
                index++;
            }

            sc.close();
        }

        Moon[] original = copyMoons(moons);

        int maxIterations = 10;
        for (int i = 0; i < maxIterations; i++) {

            doPairs(moons);
        }

        System.out.println("Energy: " + calcEnergy(moons));

        long x = getSameX(copyMoons(original));
        long y = getSameY(copyMoons(original));
        long z = getSameZ(copyMoons(original));

        long count = lcm(x, lcm(y, z)) * 2;
        System.out.printf("It took %d steps.\n", count);
    }

    private static Moon[] copyMoons(Moon[] moons) {
        Moon[] moonsClone = new Moon[4];
        for (int  i = 0 ; i < 4; i++) {
            moonsClone[i] = moons[i].clone();
        }

        return moonsClone;
    }

    private static int calcEnergy(Moon[] moons) {
        int sum = 0;
        for (int i = 0; i < 4; i++) {
            sum += moons[i].getEnergy();
        }
        return sum;
    }

    private static long getSameX(Moon[] moons) {
        long count = 0;
        while (true) {
            doPairs(moons);
            count++;

            if (moons[0].velX == 0 && moons[1].velX == 0 && moons[2].velX == 0 && moons[3].velX == 0) {
                return count;
            }
        }
    }

    private static long getSameY(Moon[] moons) {
        long count = 0;
        while (true) {
            doPairs(moons);
            count++;

            if (moons[0].velY == 0 && moons[1].velY == 0 && moons[2].velY == 0 && moons[3].velY == 0) {
                return count;
            }
        }
    }

    private static long getSameZ(Moon[] moons) {
        long count = 0;
        while (true) {
            doPairs(moons);
            count++;

            if (moons[0].velZ == 0 && moons[1].velZ == 0 && moons[2].velZ == 0 && moons[3].velZ == 0) {
                return count;
            }
        }
    }
    
    public static long lcm(long number1, long number2) {
        if (number1 == 0 || number2 == 0) {
            return 0;
        }
        long absNumber1 = Math.abs(number1);
        long absNumber2 = Math.abs(number2);
        long absHigherNumber = Math.max(absNumber1, absNumber2);
        long absLowerNumber = Math.min(absNumber1, absNumber2);
        long lcm = absHigherNumber;
        while (lcm % absLowerNumber != 0) {
            lcm += absHigherNumber;
        }
        return lcm;
    }

    private static void doPairs(Moon[] moons) {
        for (int i = 0; i < moons.length; i++) {
            for (int j = i + 1; j < moons.length; j++) {
                doGravity(moons[i], moons[j]);
            }

            moons[i].applyVelocity();
        }
    }

    private static void doGravity(Moon one, Moon two) {
        // X
        if (one.posX > two.posX) {
            one.velX--;
            two.velX++;
        } else if (one.posX < two.posX) {
            one.velX++;
            two.velX--;
        }
        // Y
        if (one.posY > two.posY) {
            one.velY--;
            two.velY++;
        } else if (one.posY < two.posY) {
            one.velY++;
            two.velY--;
        }
        // Z
        if (one.posZ > two.posZ) {
            one.velZ--;
            two.velZ++;
        } else if (one.posZ < two.posZ) {
            one.velZ++;
            two.velZ--;
        }
    }

    public static class Moon {
        int id;

        int posX;
        int posY;
        int posZ;

        int velX;
        int velY;
        int velZ;

        public Moon(int id, int posX, int posY, int posZ) {
            this.id = id;

            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;

            this.velX = 0;
            this.velY = 0;
            this.velZ = 0;
        }

        public void applyVelocityX() {
            posX += velX;
        }

        public void applyVelocity() {
            posX += velX;
            posY += velY;
            posZ += velZ;
        }

        public int getEnergy() {
            return (Math.abs(posX) + Math.abs(posY) + Math.abs(posZ))
                    * (Math.abs(velX) + Math.abs(velY) + Math.abs(velZ));
        }

        @Override
        public String toString() {
            return "posX=" + posX + ", posY=" + posY + ", posZ=" + posZ;
        }

        public String fancyString() {
            return String.format("(id %d) pos=<x=%2d, y=%2d, z=%2d>, vel=<x=%2d, y=%2d, z=%2d>", id, posX, posY, posZ,
                    velX, velY, velZ);
        }

        public Moon clone() {
            return new Moon(id, posX, posY, posZ);
        }
    }
}