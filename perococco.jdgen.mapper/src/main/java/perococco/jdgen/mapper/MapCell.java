package perococco.jdgen.mapper;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MapCell {

    public static @NonNull MapCell empty() {
        return EMPTY;
    }

    private static final MapCell EMPTY = new MapCell(CellType.EMPTY);

    @Getter
    private final @NonNull CellType type;

    public boolean isEmpty() {
        return type == CellType.EMPTY;
    }

}
