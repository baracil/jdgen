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

    public IntVector position() {
        return center;
    }

}
