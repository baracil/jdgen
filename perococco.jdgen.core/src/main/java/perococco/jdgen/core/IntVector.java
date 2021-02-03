package perococco.jdgen.core;

import lombok.NonNull;
import lombok.Value;

@Value
public class IntVector {

    public static final IntVector NIL = new IntVector(0, 0);
    int x;
    int y;

    public @NonNull IntVector add(@NonNull IntVector other) {
        return new IntVector(x+other.x, y+other.y);
    }

    public @NonNull IntVector negate() {
        return new IntVector(-x, -y);
    }

    public @NonNull double norm2() {
        return x*x+y*y;
    }

    public boolean isNil() {
        return x == 0 && y == 0;
    }
}
