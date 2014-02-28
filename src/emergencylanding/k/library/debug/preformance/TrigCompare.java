package emergencylanding.k.library.debug.preformance;

import java.util.*;

import emergencylanding.k.library.debug.FPS;
import emergencylanding.k.library.util.Maths;

public class TrigCompare {
    static double[] angles = new double[100000];
    static {
        Random r = new Random(System.nanoTime());
        List<Integer> special = Arrays.asList(new Integer[] { 0, 30, 45, 60,
                90, 180, 270 });
        for (int i = 0; i < angles.length / 2; i++) {
            angles[i] = Math.round(r.nextDouble() * 360);
        }
        for (int i = 0; i < angles.length / 2; i++) {
            angles[angles.length - i - 1] = special.get(r.nextInt(special
                    .size()));
        }
        Maths.qtan(100); // init before calculations
        Math.tan(100);
    }

    public static void main(String[] args) {
        int math = 0, maths = 0;
        while (true) {
            long m = timeMaths();
            long m2 = timeMath();
            if (m > m2) {
                math++;
            } else {
                maths++;
            }
            System.err.println(math + "/" + maths);
        }
    }

    private static long timeMaths() {
        long cos = FPS.getTime();
        for (double d : angles) {
            Maths.qcos(d);
        }
        long cose = FPS.getTime() - cos;
        long sin = FPS.getTime();
        for (double d : angles) {
            Maths.qsin(d);
        }
        long sine = FPS.getTime() - sin;
        long tan = FPS.getTime();
        for (double d : angles) {
            Maths.qtan(d);
        }
        long tane = FPS.getTime() - tan;
        return cose + sine + tane;
    }

    private static long timeMath() {
        long cos = FPS.getTime();
        for (double d : angles) {
            d = Math.toRadians(d);
            Math.cos(d);
        }
        long cose = FPS.getTime() - cos;
        long sin = FPS.getTime();
        for (double d : angles) {
            d = Math.toRadians(d);
            Math.sin(d);
        }
        long sine = FPS.getTime() - sin;
        long tan = FPS.getTime();
        for (double d : angles) {
            d = Math.toRadians(d);
            Math.tan(d);
        }
        long tane = FPS.getTime() - tan;
        return cose + sine + tane;
    }

}
