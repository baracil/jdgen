package perococco.jdgen.mapper;

import lombok.NonNull;
import perococco.jdgen.core.IntPoint;
import perococco.jdgen.core.RectanglePosition;
import perococco.jdgen.core.Size;

public interface Map {

    static Map create(Size size) {
        return ArrayMap.create(size);
    }

    MapCell getCellAt(int x, int y);

    default MapCell getCellAt(@NonNull IntPoint position) {
        return getCellAt(position.getX(),position.getY());
    }

    void setCellAt(@NonNull MapCell mapCell, int x, int y);

    default void setCellAt(@NonNull MapCell mapCell, IntPoint position) {
        setCellAt(mapCell,position.getX(),position.getY());
    }

    @NonNull Map offsetMap(int xOffset, int yOffset);

    @NonNull Map clearOffsets();

    @NonNull Size getSize();

    default void setCellAtIfEmpty(MapCell cell, IntPoint position) {
        setCellAtIfEmpty(cell,position.getX(),position.getY());
    }

    default void setCellAtIfEmpty(MapCell cell, int x, int y) {
        if (getCellAt(x,y).isEmpty()) {
            setCellAt(cell,x,y);
        }
    }
}
