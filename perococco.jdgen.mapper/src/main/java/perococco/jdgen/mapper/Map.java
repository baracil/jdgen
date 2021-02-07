package perococco.jdgen.mapper;

import lombok.NonNull;
import perococco.jdgen.core.IntPoint;
import perococco.jdgen.core.Size;

public interface Map {

    static Map create(Size size) {
        return ArrayMap.create(size);
    }

    MapCell getCellAt(int x, int y);

    void setCellAt(@NonNull MapCell mapCell, int x, int y);

    void setCellAt(@NonNull MapCell mapCell, IntPoint position);

    @NonNull Map offsetMap(int xOffset, int yOffset);

    @NonNull Map clearOffsets();


    @NonNull Size getSize();
}
