package perococco.jdgen.api._private;

import lombok.Value;
import perococco.jdgen.api.IntPoint;

@Value
public class BasicIntPoint implements IntPoint {
    int x;
    int y;
}
