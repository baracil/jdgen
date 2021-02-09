package perococco.jdgen.viewer;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.api.Cell;
import perococco.jdgen.api.CellType;

@RequiredArgsConstructor
public class FXCell implements Cell {

    @Getter
    private final @NonNull CellType type;

}
