package perococco.jdgen.mapper;


import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import perococco.jdgen.core.Rectangle;
import perococco.jdgen.core.Room;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MainRoomFiller {

    public static void fillMainRooms(@NonNull Map map, @NonNull MapGeometry mapGeometry, @NonNull ImmutableList<Room> rooms) {
        new MainRoomFiller(map, mapGeometry, rooms).fill();
    }

    private final @NonNull Map map;
    private final @NonNull MapGeometry geometry;
    private final @NonNull ImmutableList<Room> rooms;

    private void fill() {
        rooms.stream()
             .map(r -> r.getRectangle())
             .forEach(this::fillCellsForRoom);
    }

    private void fillCellsForRoom(@NonNull Rectangle rectangle) {
        rectangle.streamPositions()
                 .forEach(p -> {
                     final var cellType = p.isBorder() ? CellType.WALL : CellType.FLOOR;
                     final var cell = new Cell(cellType);
                     map.setCellAt(cell, geometry.offsetToMapCoordinates(p));
                 });
    }

}
