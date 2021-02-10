package perococco.jdgen.core;

import lombok.NonNull;
import perococco.jdgen.core._private.SimpleRectangle;

public interface Rectangle extends RectangleGeometry {

    static @NonNull Rectangle with(int xc, int yc, int halfWidth, int halfHeight) {
        return new SimpleRectangle(xc, yc, halfWidth, halfHeight);
    }

    int displacementToPutRightOf(@NonNull Rectangle reference);

    int displacementToPutLeftOf(@NonNull Rectangle reference);

    int displacementToPutAboveOf(@NonNull Rectangle reference);

    int displacementToPutBelowOf(@NonNull Rectangle reference);

    @NonNull Rectangle withPos(double posX, double posY);

    @NonNull Rectangle translate(@NonNull Vector displacement);




}
