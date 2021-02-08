package perococco.jdgen.core;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Delegate;

public class Room implements RectangleGeometry {

    @Getter
    @Delegate(types = RectangleGeometry.class)
    private final @NonNull Cell reference;

    @Getter
    private final Point2D position;

    public Room(@NonNull Cell cell) {
        this.reference = cell;
        this.position = new Point2D(cell.getXc(),cell.getYc());
    }

    @Override
    public String toString() {
        return "Room{" + getXc() +","+getYc()+'}';
    }

    public @NonNull Rectangle getContainer() {
        return reference.getContainer();
    }

}
