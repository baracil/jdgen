package perococco.jdgen.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.api.CellType;
import perococco.jdgen.api.IntPoint;
import perococco.jdgen.api.Cell;

@RequiredArgsConstructor
public class WallFiller {

    public static void fill(@NonNull MapperParameters parameters) {
        new WallFiller(parameters).fill();
    }

    private final @NonNull MapperParameters parameters;

    private void fill() {
        final var map = parameters.getMap();
        map.allMapPositions()
           .filter(this::isEmpty)
           .filter(this::shouldBeAWall)
           .forEach(this::setCellAsWall);
    }

    private boolean isEmpty(IntPoint position) {
        return getCellTypeAt(position) == CellType.EMPTY;
    }

    private boolean shouldBeAWall(IntPoint position) {
        return position.neighbours()
                .map(this::getCellTypeAt)
                .anyMatch(t -> t != CellType.EMPTY && t != CellType.WALL);
    }

    private void setCellAsWall(IntPoint position) {
        parameters.getMap().setCellAt(new Cell(CellType.WALL), position);
    }

    private @NonNull CellType getCellTypeAt(IntPoint position) {
        return parameters.getMap().getCellAt(position).getType();
    }

}
