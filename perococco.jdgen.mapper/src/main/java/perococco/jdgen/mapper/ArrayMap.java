package perococco.jdgen.mapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.api.*;

import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ArrayMap<C extends Cell> implements Map<C> {

    public static @NonNull <C extends Cell> ArrayMap<C> create(@NonNull Size size, @NonNull CellFactory<C> cellFactory) {
        final C[] cells = IntStream.range(0, size.getHeight() * size.getWidth())
                                   .mapToObj(i -> cellFactory.createCell(CellType.EMPTY))
                                   .toArray(cellFactory::arrayConstructor);
        return new ArrayMap<C>(cellFactory, size, cells);
    }

    private final @NonNull CellFactory<C> cellFactory;

    @Getter
    private final @NonNull Size size;

    private final @NonNull C[] cells;

    public C getCellAt(@NonNull Position position) {
        return cells[toLinearCoordinate(position)];
    }

    public void setCellAt(@NonNull C cell, @NonNull Position position) {
        this.checkCoordinate(position);
        cells[toLinearCoordinate(position)] = cell;
    }

    @Override
    public boolean isOutside(@NonNull Position position) {
        return position.getX() < 0 || position.getX() >= size.getWidth() || position.getY() < 0 || position.getY() >= size.getHeight();
    }

    @Override
    public @NonNull Stream<Position> allMapPositions() {
        return IntStream.range(0, cells.length).mapToObj(this::toPointCoordinate);
    }

    private int toLinearCoordinate(@NonNull Position position) {
        return position.getX() + position.getY() * size.getWidth();
    }

    private @NonNull Position toPointCoordinate(int linearCoordinate) {
        final var width = size.getWidth();
        return Position.at(linearCoordinate % width, linearCoordinate / width);
    }

    private void checkCoordinate(@NonNull Position position) {
        if (isOutside(position)) {
            throw new IllegalArgumentException("Position is outside of the map p=" + position);
        }
    }

    @Override
    public @NonNull Map<C> duplicate() {
        return new ArrayMap<>(cellFactory, size, cells.clone());
    }

    @Override
    public @NonNull Map<C> setTransformation(@NonNull Transformation transformation) {
        return new MapWithTransformation<>(this, transformation);
    }

    @Override
    public @NonNull Map<C> clearTransformation() {
        return this;
    }

}
