package perococco.jdgen.core;

import lombok.Value;
import perococco.gen.generator.IntPoint;

@Value
public class RectanglePosition implements IntPoint {

    int x;
    int y;
    boolean border;
}
