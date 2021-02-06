package perococco.jdgen.core;

import lombok.*;

@Value
public class Vector2D {

    public static @NonNull Vector2D of(double x, double y) {
        return new Vector2D(x, y);
    }

    double x;
    double y;

    @Override
    public String toString() {
        return "(" + x +", " + y +")";
    }
}
