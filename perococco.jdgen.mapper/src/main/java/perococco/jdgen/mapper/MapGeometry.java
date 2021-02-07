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

    public @NonNull IntPoint offsetToMapCoordinates(@NonNull IntPoint rectanglePosition) {
        return new IntVector(rectanglePosition.x()+xOffset,rectanglePosition.y()+yOffset);
    }
}
