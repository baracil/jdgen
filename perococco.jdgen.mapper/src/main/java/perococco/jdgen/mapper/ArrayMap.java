package perococco.jdgen.mapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.gen.generator.*;
import perococco.jdgen.core.IntVector;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ArrayMap implements Map {

    private final static MapCell EMPTY = new MapCell(CellType.EMPTY);

    public static @NonNull ArrayMap create(@NonNull Size size) {
        final MapCell[] mapCells = new MapCell[size.getHeight()*size.getWidth()];
        Arrays.fill(mapCells, MapCell.empty());
        return new ArrayMap(size, mapCells);
    }

    @Getter
    private final @NonNull Size size;

    private final @NonNull MapCell[] mapCells;

    public MapCell getCellAt(int x, int y) {
        if (this.isOutside(x, y)) {
            return EMPTY;
        }
        return mapCells[toLinearCoordinate(x, y)];
    }

    public void setCellAt(@NonNull MapCell mapCell, int x, int y) {
        this.checkCoordinate(x,y);
        mapCells[toLinearCoordinate(x, y)] = mapCell;
    }

    @Override
    public @NonNull Stream<IntPoint> allMapPositions() {
        return IntStream.range(0,mapCells.length).mapToObj(this::toPointCoordinate);
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

    public boolean isOutside(int x, int y) {
        return (x<0 || x >= size.getWidth() || y < 0 || y >= size.getHeight());
    }

    @Override
    public @NonNull Map offsetMap(int xOffset, int yOffset) {
        return new OffsetedMap(this,xOffset,yOffset);
    }

    @Override
    public @NonNull Map clearOffsets() {
        return this;
    }
}
