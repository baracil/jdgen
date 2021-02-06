package perococco.jdgen.core;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@Value
@EqualsAndHashCode(of = {"x","y"})
public class Point2D {

    public static @NonNull Point2D of(double x, double y) {
        return new Point2D(x,y);
    }

    double x;
    double y;
    double d2;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
        this.d2 = x*x+y*y;
    }

    public double distanceToOriginSquared() {
        return d2;
    }
}
