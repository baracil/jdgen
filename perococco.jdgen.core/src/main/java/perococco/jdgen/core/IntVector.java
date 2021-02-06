package perococco.jdgen.core;

import lombok.Value;

@Value
public class IntVector {

    public static final IntVector NIL = new IntVector(0, 0);
    int x;
    int y;

    @Override
    public String toString() {
        return "(" + x +", " + y +")";
    }
}
