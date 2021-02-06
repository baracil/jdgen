package perococco.jdgen.core;

import lombok.Getter;
import lombok.NonNull;

public class Room {

    @Getter
    private final @NonNull Rectangle geometry;

    private final @NonNull IntVector center;

    public Room(@NonNull Rectangle geometry) {
        this.geometry = geometry;
        this.center = new IntVector(geometry.xc(),geometry.yc());
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
