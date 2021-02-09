package perococco.gen.generator._private;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.api.Cell;
import perococco.jdgen.api.CellFactory;
import perococco.jdgen.api.CellType;

@RequiredArgsConstructor
public class SimpleCell implements Cell {

    @Getter
    private final @NonNull CellType type;


    public static class Factory implements CellFactory<Cell> {
        @Override
        public @NonNull Cell createCell(@NonNull CellType cellType) {
            return new SimpleCell(cellType);
        }

        @Override
        public @NonNull Cell[] arrayConstructor(int size) {
            return new SimpleCell[size];
        }
    }


}
