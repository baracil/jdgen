package perococco.jdgen.mapper;


import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.RectangleGeometry;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MainRoomFiller {

    public static void fillMainRooms(@NonNull MapperParameters parameters) {
        new MainRoomFiller(parameters).fill();
    }

    private final @NonNull MapperParameters parameters;

    private void fill() {
        parameters.forEachRooms(this::fillCellsForRoom);
    }

    private void fillCellsForRoom(@NonNull RectangleGeometry rectangle) {
        rectangle.streamPositionsWithoutBorders()
                 .forEach(p -> {
                     final var cellType = p.isBorder() ? CellType.WALL : CellType.FLOOR;
                     final var cell = new MapCell(cellType);
                     parameters.getMap().setCellAt(cell, p);
                 });
    }

}
