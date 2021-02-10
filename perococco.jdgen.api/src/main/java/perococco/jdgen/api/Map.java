package perococco.jdgen.api;

import lombok.NonNull;

import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public interface Map<C extends Cell> {

    /**
     * @return a deep copy of this map.
     */
    @NonNull Map<C> duplicate();

    /**
     * @param x the x coordinate of the cell
     * @param y the y coordinate of the cell
     * @return the cell at the requested coordinate. If the coordinates are outside of the map, an empty cell will be return
     */
    @NonNull C getCellAt(int x, int y);

    /**
     * @return the size of the map
     */
    @NonNull Size getSize();

    /**
     * @return a stream of all the positions on the map
     */
    @NonNull Stream<Position> allMapPositions();

    /**
     * @param cell the cell to set
     * @param x the x coordinate of the cell to set
     * @param y the y coordinate of the cell to set
     */
    void setCellAt(@NonNull C cell, int x, int y);

    /**
     * @param x the x coordinate
     * @param y the y coordinate
     * @return true if the point at x,y is outside of the map
     */
    default boolean isOutside(int x, int y) {
        final var size = getSize();
        return x < 0 || x >= size.getWidth() || y < 0 || y >= size.getHeight();
    }

    /**
     * @param position the requested position
     * @return the cell at the provided position. If the position is outside of the map, an empty cell is returned
     */
    default @NonNull C getCellAt(@NonNull Position position) {
        return getCellAt(position.getX(), position.getY());
    }

    /**
     * @param cell the cell to set
     * @param position the position of the cell to set
     */
    default void setCellAt(@NonNull C cell, @NonNull Position position) {
        updateCell(c -> cell, position);
    }

    /**
     * Set the cell at the provided position to the provided value if the current cell is empty
     * @param cell the cell to set if the current cell is empty
     * @param position the position of the cell to set
     */
    default void setCellAtIfEmpty(@NonNull C cell, @NonNull Position position) {
        setCellAtIfEmpty(cell, position.getX(), position.getY());
    }

    /**
     * Set the cell at the provided position to the provided value if the current cell is empty
     * @param cell the cell to set if the current cell is empty
     * @param x the x position of the cell to set
     * @param y the y position of the cell to set
     */
    default void setCellAtIfEmpty(@NonNull C cell, int x, int y) {
        updateCell(c -> c.isEmpty() ? cell : c, x, y);
    }

    /**
     * @param cellUpdater the cell updater
     * @param position the position where the update will be done
     */
    default void updateCell(@NonNull UnaryOperator<C> cellUpdater, @NonNull Position position) {
        updateCell(cellUpdater, position.getX(), position.getY());
    }

    /**
     * @param cellUpdater the cell updater
     * @param x the x position where the update will be done
     * @param y the y position where the update will be done
     */
    default void updateCell(@NonNull UnaryOperator<C> cellUpdater, int x, int y) {
        final var cell = getCellAt(x, y);
        final var newCell = cellUpdater.apply(cell);
        setCellAt(newCell, x, y);
    }

    /**
     * @param position the requested position
     * @return true if the position is outside of the map
     */
    default boolean isOutside(@NonNull Position position) {
        return isOutside(position.getX(), position.getY());
    }

}
