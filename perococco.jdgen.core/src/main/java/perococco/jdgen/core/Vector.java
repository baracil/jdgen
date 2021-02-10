package perococco.jdgen.core;

import lombok.NonNull;
import lombok.Value;
import perococco.jdgen.api.Position;

@Value
public class Vector {

    public static final Vector NIL = new Vector(0, 0);
    int x;
    int y;

    @Override
    public String toString() {
        return "(" + x +", " + y +")";
    }
}
