package perococco.jdgen.core;

import lombok.NonNull;
import lombok.Value;

@Value
public class Rectangle {

    int x;
    int y;

    int width;
    int height;


    public boolean overlap(@NonNull Rectangle other) {
        boolean xOverlap = other.x <= x + width-1 && x <= other.x + other.width-1;
        boolean yOverlap = other.y <= y + height-1 && y <= other.y + other.height-1;
        return xOverlap && yOverlap;
    }


    public @NonNull Rectangle withPos(double posX, double posY) {
        return new Rectangle((int)Math.floor(posX),(int)Math.floor(posY),width,height);
    }
}
