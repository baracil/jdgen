package perococco.jdgen.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.api.CellType;
import perococco.jdgen.api.Position;

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

    private boolean isEmpty(Position position) {
        return parameters.getCellTypeAt(position) == CellType.EMPTY;
    }

    private boolean shouldBeAWall(Position position) {
        return position.neighbours()
                .map(parameters::getCellTypeAt)
                .anyMatch(t -> t != CellType.EMPTY && t != CellType.WALL);
    }

    private void setCellAsWall(Position position) {
        parameters.getMap().setCellAt(parameters.createCell(CellType.WALL), position);
    }

}
