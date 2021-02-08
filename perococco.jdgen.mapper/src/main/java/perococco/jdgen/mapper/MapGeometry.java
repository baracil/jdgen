package perococco.jdgen.mapper;

import lombok.NonNull;
import lombok.Value;
import perococco.jdgen.api.Size;

@Value
public class MapGeometry {

    int xOffset;
    int yOffset;

    @NonNull Size size;

}
