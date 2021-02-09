package perococco.jdgen.mapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.api.*;
import perococco.jdgen.core.IntVector;

import java.util.stream.IntStream;
import java.util.stream.Stream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ArrayMap<C extends Cell> implements MapInConstruction<C> {

    public static @NonNull <C extends Cell> ArrayMap<C> create(@NonNull Size size, @NonNull CellFactory<C> cellFactory) {
        final C[] cells = IntStream.range(0, size.getHeight()*size.getWidth())
                                      .mapToObj(i -> cellFactory.createCell(CellType.EMPTY))
                                      .toArray(cellFactory::arrayConstructor);
        return new ArrayMap<C>(cellFactory, size, cells);
    }

    private final @NonNull CellFactory<C> cellFactory;

    @Getter
    private final @NonNull Size size;

    private final @NonNull C[] cells;

    public C getCellAt(int x, int y) {
        return cells[toLinearCoordinate(x, y)];
    }

    public void setCellAt(@NonNull C cell, int x, int y) {
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
    public @NonNull MapInConstruction<C> duplicate() {
        return new ArrayMap<>(cellFactory,size, cells.clone());
    }

    @Override
    public @NonNull MapInConstruction<C> offsetMap(int xOffset, int yOffset) {
        return new OffsetedMap<>(this,xOffset,yOffset);
    }

    @Override
    public @NonNull MapInConstruction<C> clearOffsets() {
        return this;
    }

    @Override
    public @NonNull C createCell(@NonNull CellType cellType) {
        return cellFactory.createCell(cellType);
    }
}
