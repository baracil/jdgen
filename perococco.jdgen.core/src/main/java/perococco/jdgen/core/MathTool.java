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

    public static @NonNull DoubleSupplier normalDistribution(@NonNull RandomGenerator randomGenerator, int minValue, int maxValue) {
        final double std = (maxValue-minValue);
        final var normalDistribution = new NormalDistribution(randomGenerator,minValue,std);
        return normalDistribution::sample;
    }
}
