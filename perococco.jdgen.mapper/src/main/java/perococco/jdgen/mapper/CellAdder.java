package perococco.jdgen.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.api.CellType;
import perococco.jdgen.core.Cell;

@RequiredArgsConstructor
public class CellAdder<C extends perococco.jdgen.api.Cell> {

    public static <C extends perococco.jdgen.api.Cell> void addCells(@NonNull MapperParameters<C> parameters) {
        new CellAdder<>(parameters).addCells();
    }

    private final @NonNull MapperParameters<C> parameters;

    private void addCells() {
        parameters.cellStream()
                  .filter(this::overlapACorridor)
                  .forEach(OneRoomFiller.createFiller(parameters, CellType.CELL_FLOOR, true));
    }

    private boolean overlapACorridor(@NonNull Cell cell) {
        final var map = parameters.getMap();
        final boolean outside = cell.streamPositions().anyMatch(map::isOutside);
        if (outside) {
            return false;
        }
        return cell.streamPositions()
            .anyMatch(p -> map.getCellAt(p).isCorridor());
    }

}
