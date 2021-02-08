package perococco.gen.generator._private;

import lombok.Value;
import perococco.gen.generator.IntPoint;

@Value
public class BasicIntPoint implements IntPoint {
    int x;
    int y;
}
