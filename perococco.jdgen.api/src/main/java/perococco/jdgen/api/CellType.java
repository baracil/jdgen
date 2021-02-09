package perococco.jdgen.api;

public enum CellType {
    WALL,
    ROOM_FLOOR,
    CORRIDOR_FLOOR,
    CELL_FLOOR,
    DOOR,
    EMPTY,
    ;

    public boolean isFloor() {
        return this == CORRIDOR_FLOOR || this == ROOM_FLOOR || this == CELL_FLOOR;
    }
}
