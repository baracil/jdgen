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
     * @return the size of the map
     */
    @NonNull Size getSize();

    /**
     * @return a stream of all the positions on the map
     */
    @NonNull Stream<Position> allMapPositions();

    /**
     * @param position the requested position
     * @return the cell at the provided position. If the position is outside of the map, an empty cell is returned
     */
    @NonNull C getCellAt(@NonNull Position position);


    /**
     * @param cell the cell to set
     * @param position the position of the cell to set
     */
    void setCellAt(@NonNull C cell, @NonNull Position position);

    /**
     * @param position the requested position
     * @return true if the position is outside of the map
     */
    boolean isOutside(@NonNull Position position);

    /**
     * Set the cell at the provided position to the provided value if the current cell is empty
     * @param cell the cell to set if the current cell is empty
     * @param position the position of the cell to set
     */
    default void setCellAtIfEmpty(@NonNull C cell, @NonNull Position position) {
        updateCell(c -> c.isEmpty() ? cell : c, position);
    }

    /**
     * @param cellUpdater the cell updater
     * @param position the position where the update will be done
     */
    default void updateCell(@NonNull UnaryOperator<C> cellUpdater, @NonNull Position position) {
        final var cell = getCellAt(position);
        final var newCell = cellUpdater.apply(cell);
        setCellAt(newCell, position);
    }

    @NonNull Map<C> setTransformation(@NonNull Transformation transformation);

    @NonNull Map<C> clearTransformation();


}
