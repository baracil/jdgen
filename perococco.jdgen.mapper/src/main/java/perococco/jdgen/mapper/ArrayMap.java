package perococco.jdgen.mapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.api.*;
import perococco.jdgen.core.IntVector;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ArrayMap implements OffsetableMap {

    private final static Cell EMPTY = new Cell(CellType.EMPTY);

    public static @NonNull ArrayMap create(@NonNull Size size) {
        final Cell[] cells = new Cell[size.getHeight()*size.getWidth()];
        Arrays.fill(cells, Cell.empty());
        return new ArrayMap(size, cells);
    }

    @Getter
    private final @NonNull Size size;

    private final @NonNull Cell[] cells;

    public Cell getCellAt(int x, int y) {
        if (this.isOutside(x, y)) {
            return EMPTY;
        }
        return cells[toLinearCoordinate(x, y)];
    }

    public void setCellAt(@NonNull Cell cell, int x, int y) {
        this.checkCoordinate(x,y);
        cells[toLinearCoordinate(x, y)] = cell;
    }

    @Override
    public @NonNull Stream<IntPoint> allMapPositions() {
        return IntStream.range(0, cells.length).mapToObj(this::toPointCoordinate);
    }

    private int toLinearCoordinate(int x, int y) {
        return x+y*size.getWidth();
    }

    private @NonNull IntPoint toPointCoordinate(int linearCoordinate) {
        final var width = size.getWidth();
        return new IntVector(linearCoordinate%width, linearCoordinate/width);
    }

    private void checkCoordinate(int x, int y) {
        if (isOutside(x, y)) {
            throw new IllegalArgumentException("Coordinate are outside of the map x='"+x+"' y='"+y+"'");
        }
    }

    @Override
    public @NonNull OffsetableMap duplicate() {
        return new ArrayMap(size, cells.clone());
    }

    @Override
    public @NonNull OffsetableMap offsetMap(int xOffset, int yOffset) {
        return new OffsetedMap(this,xOffset,yOffset);
    }

    @Override
    public @NonNull OffsetableMap clearOffsets() {
        return this;
    }
}
