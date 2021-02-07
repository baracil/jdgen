package perococco.jdgen.core;

import lombok.Getter;
import lombok.NonNull;

public class Room {

    @Getter
    private final @NonNull Rectangle rectangle;

    private final @NonNull IntVector center;

    public Room(@NonNull Rectangle rectangle) {
        this.rectangle = rectangle;
        this.center = new IntVector(rectangle.xc(), rectangle.yc());
    }

    public @NonNull Point2D position() {
        return Point2D.of(center.x(), center.y());
    }

    @Override
    public String toString() {
        return "Room{" + center +
                '}';
    }
}
