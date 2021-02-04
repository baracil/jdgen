package perococco.jdgen.graph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class Determinant2DTest {

    public static Stream<Arguments> samples() {
        return Stream.of(
                Arguments.of(12,1,2,4,2,2,6),
                Arguments.of(0,1,2,3,4,5,6)
        );
    }

    @ParameterizedTest
    @MethodSource("samples")
    public void shouldHaveRightDeterminant(double expectedValue,
                                           double xa, double ya,
                                           double xb, double yb,
                                           double xc, double yc) {
        final double actual = Tools.determinant2D(xa, ya, xb, yb, xc, yc);

        Assertions.assertEquals(expectedValue,actual,1e-6);
    }
}
