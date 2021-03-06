package perococco.jdgen.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import perococco.jdgen.api.Position;

import java.util.Set;
import java.util.stream.Collectors;

public class RectanglePositionTest {

    @Test
    public void shouldHaveRightRectanglePositions() {
        final Rectangle rectangle = Rectangle.with(2, 2, 2, 2);

        final Set<Position> positions = rectangle.streamPositions().collect(Collectors.toSet());

        Assertions.assertEquals(25,positions.size());
    }
}
