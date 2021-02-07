package perococco.jdgen.graph;

import lombok.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import perococco.jdgen.core.Point2D;

import java.util.stream.Stream;

public class CircumscribedCircleTest {


    //12*12 = 144
    //13*13 = 169
    private static Stream<Arguments> pointSampleForRadius() {
        return Stream.of(Arguments.of(2.5, Point2D.of(0, 0), Point2D.of(3, 0), Point2D.of(0, 4)),
                         Arguments.of(2.5, Point2D.of(0, 0), Point2D.of(4, 0), Point2D.of(0, 3)),
                         Arguments.of(6.5, Point2D.of(0, 0), Point2D.of(5, 0), Point2D.of(0, 12))
        );
    }

    private static Stream<Arguments> pointSampleForXPosition() {
        return Stream.of(Arguments.of(1.5, Point2D.of(0, 0), Point2D.of(3, 0), Point2D.of(0, 4)),
                         Arguments.of(2, Point2D.of(0, 0), Point2D.of(4, 0), Point2D.of(0, 3)),
                         Arguments.of(2.5, Point2D.of(0, 0), Point2D.of(5, 0), Point2D.of(0, 12))
        );
    }

    private static Stream<Arguments> pointSampleForYPosition() {
        return Stream.of(Arguments.of(2, Point2D.of(0, 0), Point2D.of(3, 0), Point2D.of(0, 4)),
                         Arguments.of(1.5, Point2D.of(0, 0), Point2D.of(4, 0), Point2D.of(0, 3)),
                         Arguments.of(6, Point2D.of(0, 0), Point2D.of(5, 0), Point2D.of(0, 12))
        );
    }

    @ParameterizedTest
    @MethodSource("pointSampleForRadius")
    public void shouldHaveRadius(double expectedRadius, @NonNull Point2D a, @NonNull Point2D b, @NonNull Point2D c) {
        final var circle = Circle.circumscribedCircleOf(a, b, c);
        Assertions.assertEquals(expectedRadius, circle.getRadius(), 1e-6);
    }

    @ParameterizedTest
    @MethodSource("pointSampleForXPosition")
    public void shouldHaveCorrectXCenter(double expectedX, @NonNull Point2D a, @NonNull Point2D b, @NonNull Point2D c) {
        final var circle = Circle.circumscribedCircleOf(a, b, c);
        Assertions.assertEquals(expectedX, circle.getCenter().getX(), 1e-6);
    }
    @ParameterizedTest
    @MethodSource("pointSampleForYPosition")
    public void shouldHaveCorrectYCenter(double expectedY, @NonNull Point2D a, @NonNull Point2D b, @NonNull Point2D c) {
        final var circle = Circle.circumscribedCircleOf(a, b, c);
        Assertions.assertEquals(expectedY, circle.getCenter().getY(), 1e-6);
    }
}
