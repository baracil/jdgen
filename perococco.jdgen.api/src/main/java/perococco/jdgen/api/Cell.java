package perococco.jdgen.api;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Cell {

    public static @NonNull Cell empty() {
        return EMPTY;
    }

    private static final Cell EMPTY = new Cell(CellType.EMPTY);

    @Getter
    private final @NonNull CellType type;

    public boolean isEmpty() {
        return type == CellType.EMPTY;
    }

    public boolean isCorridor() {
        return type == CellType.CORRIDOR_FLOOR;
    }

    public boolean isFloor() {
        return type.isFloor();
    }

    public boolean isRoom() {
        return type == CellType.ROOM_FLOOR;
    }
}
