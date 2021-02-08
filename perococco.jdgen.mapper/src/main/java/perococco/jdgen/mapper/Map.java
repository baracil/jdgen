package perococco.jdgen.mapper;

import lombok.NonNull;
import perococco.jdgen.core.IntPoint;
import perococco.jdgen.core.Size;

import java.util.function.UnaryOperator;

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

    default void updateCell(@NonNull UnaryOperator<MapCell> cellUpdate, int x, int y) {
        final var cell = getCellAt(x,y);
        final var newCell = cellUpdate.apply(cell);
        setCellAt(newCell,x,y);
    }

    default boolean isOutside(@NonNull IntPoint position) {
        return isOutside(position.getX(), position.getY());
    }

    boolean isOutside(int x, int y);
}
