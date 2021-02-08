package perococco.jdgen.core;

import lombok.NonNull;

import java.util.Random;
import java.util.function.DoubleSupplier;

public class MathTool {

    public static int makeOdd(int value) {
        if ((value%2) == 1) {
            return value;
        }
        return value+1;
    }

    public static @NonNull DoubleSupplier normalDistribution(@NonNull Random randomGenerator, int minValue, int maxValue) {
        final double std = Math.max(minValue*0.01,(maxValue-minValue));
        final var normalDistribution = new NormalDistribution(randomGenerator,minValue,std);
        return normalDistribution::sample;
    }
}
