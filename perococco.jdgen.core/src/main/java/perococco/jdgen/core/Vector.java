package perococco.jdgen.core;

import lombok.NonNull;
import lombok.Value;

@Value
public class Vector {

    public static final Vector NIL = new Vector(0,0);
    int x;
    int y;

    public @NonNull Vector add(@NonNull Vector other) {
        return new Vector(x+other.x,y+other.y);
    }

//    public @NonNull Vector subtract(@NonNull Vector other) {
//        return new Vector(x+other.x,y+other.y);
//    }
//
//    public @NonNull Vector scale(double factor) {
//        return new Vector(x*factor,y*factor);
//    }
//
    public @NonNull Vector negate() {
        return new Vector(-x,-y);
    }

    public @NonNull double norm2() {
        return x*x+y*y;
    }

    public boolean isNil() {
        return x == 0 && y == 0;
    }
}
