package perococco.jdgen.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

public class RectanglePositionTest {

    @Test
    public void shouldHaveRightRectanglePositions() {
        final Rectangle rectangle = new Rectangle(2,2,2,2);

        final Set<RectanglePosition> positions = rectangle.streamPositions().collect(Collectors.toSet());

        Assertions.assertEquals(25,positions.size());
    }
}
