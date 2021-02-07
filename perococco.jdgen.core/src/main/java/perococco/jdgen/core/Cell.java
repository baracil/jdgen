package perococco.jdgen.core;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.Delegate;

@EqualsAndHashCode(of="geometry")
public class Cell implements RectangleGeometry {

    @Delegate(types = RectangleGeometry.class)
    private final @NonNull Rectangle geometry;

    public Cell(@NonNull Rectangle geometry) {
        this.geometry = geometry;
    }


    public @NonNull Cell withPos(int x, int y) {
        return new Cell(geometry.withPos(x,y));
    }

    public @NonNull Rectangle getContainer() {
        return geometry;
    }
}
