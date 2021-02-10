package perococco.jdgen.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.api.*;

import java.util.stream.Stream;

@RequiredArgsConstructor
public class MapWithTransformation<C extends Cell> implements Map<C> {

    private final @NonNull Map<C> delegate;
    private final Transformation transformation;

    @Override
    public @NonNull Map<C> duplicate() {
        return new MapWithTransformation<>(delegate.duplicate(), transformation);
    }

    @Override
    public @NonNull Size getSize() {
        return delegate.getSize();
    }

    @Override
    public @NonNull C getCellAt(@NonNull Position position) {
        return delegate.getCellAt(transformation.apply(position));
    }

    @Override
    public void setCellAt(@NonNull C cell, @NonNull Position position) {
        delegate.setCellAt(cell, transformation.apply(position));
    }

    @Override
    public @NonNull Map<C> setTransformation(@NonNull Transformation transformation) {
        return new MapWithTransformation<>(this,transformation);
    }

    @Override
    public @NonNull Map<C> clearTransformation() {
        return delegate.clearTransformation();
    }

    @Override
    public @NonNull Stream<Position> allMapPositions() {
        return delegate.allMapPositions().map(transformation.inverse());
    }

    @Override
    public boolean isOutside(@NonNull Position position) {
        return delegate.isOutside(transformation.apply(position));
    }

}
