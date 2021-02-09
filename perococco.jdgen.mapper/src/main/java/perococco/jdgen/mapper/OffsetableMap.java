package perococco.jdgen.mapper;

import lombok.NonNull;
import perococco.jdgen.api.Map;

public interface OffsetableMap extends Map {

    @NonNull OffsetableMap offsetMap(int xOffset, int yOffset);

    @NonNull Map clearOffsets();

    @Override
    @NonNull OffsetableMap duplicate();
}
