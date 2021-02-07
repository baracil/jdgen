package perococco.jdgen.core;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
public class RectanglePosition  implements IntPoint {

    int x;
    int y;
    boolean border;
}
