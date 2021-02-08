package perococco.jdgen.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class OverlapTest {

    public static Stream<Arguments> rectangles() {
        return Stream.of(
                Arguments.of(Rectangle.with(0,0,1,1), Rectangle.with(10,0,1,2),new Overlap(-1,1))
        );
    }

    @ParameterizedTest
    @MethodSource("rectangles")
    public void testOverlapOnY(RectangleGeometry r1, RectangleGeometry r2, Overlap expected) {
        final Overlap actual = r1.computeOverlap(r2,RectangleGeometry.Y_AXIS_GETTER).orElse(null);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected,actual);
    }
}
