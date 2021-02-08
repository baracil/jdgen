package perococco.jdgen.core;

import lombok.Value;
import perococco.jdgen.api.IntPoint;

@Value
public class RectanglePosition implements IntPoint {

    int x;
    int y;
    boolean border;
}
