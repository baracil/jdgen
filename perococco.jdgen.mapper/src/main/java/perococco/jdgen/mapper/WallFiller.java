package perococco.jdgen.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.api.CellType;
import perococco.jdgen.api.IntPoint;

@RequiredArgsConstructor
public class WallFiller<C extends perococco.jdgen.api.Cell> {

    public static <C extends perococco.jdgen.api.Cell> void fill(@NonNull MapperParameters<C> parameters) {
        new WallFiller(parameters).fill();
    }

    private final @NonNull MapperParameters<C> parameters;

    private void fill() {
        final var map = parameters.getMap();
        map.allMapPositions()
           .filter(this::isEmpty)
           .filter(this::shouldBeAWall)
           .forEach(this::setCellAsWall);
    }

    private boolean isEmpty(IntPoint position) {
        return parameters.getCellTypeAt(position) == CellType.EMPTY;
    }

    private boolean shouldBeAWall(IntPoint position) {
        return position.neighbours()
                .map(parameters::getCellTypeAt)
                .anyMatch(t -> t != CellType.EMPTY && t != CellType.WALL);
    }

    private void setCellAsWall(IntPoint position) {
        parameters.getMap().setCellAt(parameters.createCell(CellType.WALL), position);
    }

}
