package perococco.jdgen.core;

import lombok.NonNull;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.RandomGenerator;

import java.util.function.DoubleSupplier;

public class MathTool {

    public static int makeOdd(int value) {
        if ((value%2) == 1) {
            return value;
        }
        return value+1;
    }

    public static boolean overlap(int x1, int length1, int x2, int length2) {
        return x1<=x2+length2 && x2 <= x1+length1;
    }

    public static boolean hoverlap(int x1, int halfLength1, int x2, int halfLength2) {
        return Math.abs(x1-x2) <= (halfLength1+halfLength2);
    }

    public static double norm2(double x, double y) {
        return x*x+y*y;
    }

    public static int separator(int x1, int length1, int x2, int length2) {
        if (x1>x2+length2) {
            return 0;
        } else if (x1+length1<x2) {
            return 0;
        }

        int d1 = (x1+length1+1) - x2;
        int d2 = (x2+length2+1) - x1;

        if (d2>d1) {
            return d1;
        }
        return -d2;
    }

    public int minMax(int value, int minimum, int maximum) {
        return Math.min(maximum,Math.max(minimum,value));
    }

    public static @NonNull DoubleSupplier normalDistribution(@NonNull RandomGenerator randomGenerator, int minValue, int maxValue) {
        final double std = (maxValue-minValue);
        final var normalDistribution = new NormalDistribution(randomGenerator,minValue,std);
        return normalDistribution::sample;
    }
}
