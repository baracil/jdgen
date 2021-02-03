package perococco.jdgen.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class TestMathTool {

    public static Stream<Arguments> separationSamples() {
        return Stream.of(
                Arguments.of(0,10,2,20,9),
                Arguments.of(0,10,2,2,-5),
                Arguments.of(0,2,2,2,1),
                Arguments.of(0,2,-3,5,-3),
                Arguments.of(-30,11,-40,15,6)
        );
    }

    @ParameterizedTest
    @MethodSource("separationSamples")
    public void testSeparation(int x1, int width1, int x2, int width2, int expectedDx) {
        final int actualDx = MathTool.separator(x1,width1,x2,width2);
        Assertions.assertEquals(expectedDx,actualDx);
    }


    public static Stream<Arguments> overlapSamples() {
        return Stream.of(
                Arguments.of(0,10,2,20,true),
                Arguments.of(0,10,2,2,true),
                Arguments.of(0,2,2,2,true),
                Arguments.of(0,2,-3,5,true),
                Arguments.of(0,10,1,2,true),
                Arguments.of(0,2,3,5,false),
                Arguments.of(3,5,0,2,false)
        );
    }

    @ParameterizedTest
    @MethodSource("overlapSamples")
    public void testOverlap(int x1, int width1, int x2, int width2, boolean expected) {
        final var actual = MathTool.overlap(x1,width1,x2,width2);
        Assertions.assertEquals(expected,actual);
    }
}
