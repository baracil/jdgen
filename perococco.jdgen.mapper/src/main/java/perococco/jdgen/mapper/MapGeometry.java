package perococco.jdgen.mapper;

import lombok.NonNull;
import lombok.Value;
import perococco.gen.generator.Size;

@Value
public class MapGeometry {

    int xOffset;
    int yOffset;

    @NonNull Size size;

}
