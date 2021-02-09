package perococco.jdgen.mapper;

import lombok.NonNull;
import perococco.jdgen.api.Cell;
import perococco.jdgen.api.CellType;
import perococco.jdgen.api.Map;

public interface MapInConstruction<C extends Cell> extends Map<C> {

    @NonNull MapInConstruction<C> offsetMap(int xOffset, int yOffset);

    @NonNull Map<C> clearOffsets();

    @Override
    @NonNull MapInConstruction<C> duplicate();

    @NonNull C createCell(@NonNull CellType cellType);
}
