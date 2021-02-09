package perococco.jdgen.api;

import lombok.NonNull;

import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public interface Map {

    @NonNull Map duplicate();

    MapCell getCellAt(int x, int y);

    @NonNull Size getSize();

    @NonNull Stream<IntPoint> allMapPositions();

    void setCellAt(@NonNull MapCell mapCell, int x, int y);

    default boolean isOutside(int x, int y) {
        final var size = getSize();
        return x < 0 || x >= size.getWidth() || y < 0 || y >= size.getHeight();
    }

    default MapCell getCellAt(@NonNull IntPoint position) {
        return getCellAt(position.getX(), position.getY());
    }

    default void setCellAt(@NonNull MapCell mapCell, IntPoint position) {
        updateCell(c -> mapCell, position);
    }

    default void setCellAtIfEmpty(@NonNull MapCell cell, IntPoint position) {
        setCellAtIfEmpty(cell, position.getX(), position.getY());
    }

    default void setCellAtIfEmpty(@NonNull MapCell cell, int x, int y) {
        updateCell(c -> c.isEmpty() ? cell : c, x, y);
    }

    default void updateCell(@NonNull UnaryOperator<MapCell> cellUpdate, @NonNull IntPoint position) {
        updateCell(cellUpdate, position.getX(), position.getY());
    }

    default void updateCell(@NonNull UnaryOperator<MapCell> cellUpdate, int x, int y) {
        final var cell = getCellAt(x, y);
        final var newCell = cellUpdate.apply(cell);
        setCellAt(newCell, x, y);
    }

    default boolean isOutside(@NonNull IntPoint position) {
        return isOutside(position.getX(), position.getY());
    }

}
