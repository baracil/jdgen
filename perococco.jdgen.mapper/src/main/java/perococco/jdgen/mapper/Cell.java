package perococco.jdgen.mapper;

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

}
