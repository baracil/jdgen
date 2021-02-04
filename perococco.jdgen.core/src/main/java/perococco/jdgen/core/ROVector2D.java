package perococco.jdgen.core;

import lombok.*;

public interface ROVector2D extends Point2D {

    double x();
    double y();

    @NonNull ROVector2D duplicate();

    @NonNull ImmutableVector2D toImmutable();

    double norm2();

}
