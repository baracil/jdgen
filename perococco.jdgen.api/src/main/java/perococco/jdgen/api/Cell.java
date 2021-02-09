package perococco.jdgen.api;

import lombok.NonNull;

public interface Cell {

    @NonNull CellType getType();

    default boolean isEmpty() {
        return getType() == CellType.EMPTY;
    }

    default boolean isCorridor() {
        return getType() == CellType.CORRIDOR_FLOOR;
    }

    default boolean isFloor() {
        return getType().isFloor();
    }

    default boolean isRoom() {
        return getType() == CellType.ROOM_FLOOR;
    }
}
