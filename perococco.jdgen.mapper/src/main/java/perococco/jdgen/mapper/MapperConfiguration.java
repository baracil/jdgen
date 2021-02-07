package perococco.jdgen.mapper;

import lombok.NonNull;
import lombok.Value;
import perococco.jdgen.core.IntPoint;
import perococco.jdgen.core.IntVector;
import perococco.jdgen.core.JDGenConfiguration;
import perococco.jdgen.core.Point2D;

import java.util.Random;

@Value
public class MapperConfiguration {

    @NonNull JDGenConfiguration configuration;

    @NonNull Random random;

    @NonNull MapGeometry geometry;

    public @NonNull IntPoint offsetToMapCoordinates(@NonNull IntPoint point) {
        return geometry.offsetToMapCoordinates(point);
    }
}
