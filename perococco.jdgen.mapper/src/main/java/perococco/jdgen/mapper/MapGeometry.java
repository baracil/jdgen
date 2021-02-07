package perococco.jdgen.mapper;

import lombok.NonNull;
import lombok.Value;
import perococco.jdgen.core.IntPoint;
import perococco.jdgen.core.IntVector;
import perococco.jdgen.core.Size;

@Value
public class MapGeometry {

    int xOffset;
    int yOffset;

    @NonNull Size size;

}
