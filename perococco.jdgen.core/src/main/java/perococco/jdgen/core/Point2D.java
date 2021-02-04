package perococco.jdgen.core;

import lombok.NonNull;

public interface Point2D {

    static @NonNull Point2D of(double x, double y) {
        return new SimplePoint2D(x,y);
    }

    double x();
    double y();

    default double distanceToOriginSquared() {
        return x()*x()+y()*y();
    }
}
