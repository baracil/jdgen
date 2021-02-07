package perococco.jdgen.mapper;


import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.RectangleGeometry;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RoomFiller {

    public static void fillMainRooms(@NonNull MapperParameters parameters, @NonNull CellType cellType) {
        new RoomFiller(parameters, cellType).fill();
    }

    private final @NonNull MapperParameters parameters;

    private final @NonNull CellType cellType;

    private void fill() {
        parameters.forEachRooms(this::fillCellsForRoom);
    }

    private void fillCellsForRoom(@NonNull RectangleGeometry rectangle) {
        rectangle.streamPositionsWithoutBorders()
                 .forEach(p -> {
                     final var cell = new MapCell(cellType);
                     parameters.getMap().setCellAtIfEmpty(cell, p);
                 });
    }

}
