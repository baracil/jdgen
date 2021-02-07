package perococco.jdgen.core;

import lombok.NonNull;
import lombok.Value;

@Value
public class IntVector implements IntPoint {

    public static final IntVector NIL = new IntVector(0, 0);
    int x;
    int y;

    public @NonNull IntVector offset(int xoffset, int yoffset) {
        return new IntVector(x+xoffset, y+yoffset);
    }

    @Override
    public String toString() {
        return "(" + x +", " + y +")";
    }
}
