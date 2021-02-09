package perococco.jdgen.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.api.*;

import java.util.stream.Stream;

@RequiredArgsConstructor
public class OffsetedMap<C extends Cell> implements MapInConstruction<C> {

    private final @NonNull MapInConstruction<C> delegate;
    private final int xOffset;
    private final int yOffset;

    @Override
    public @NonNull MapInConstruction<C> duplicate() {
        return new OffsetedMap<>(delegate.duplicate(),xOffset,yOffset);
    }

    @Override
    public @NonNull Size getSize() {
        return delegate.getSize();
    }

    @Override
    public C getCellAt(int x, int y) {
        return delegate.getCellAt(x+xOffset,y+yOffset);
    }

    @Override
    public void setCellAt(@NonNull C cell, int x, int y) {
        delegate.setCellAt(cell, x+xOffset, y+yOffset);
    }

    @Override
    public @NonNull MapInConstruction<C> offsetMap(int xOffset, int yOffset) {
        return new OffsetedMap<>(delegate,this.xOffset+xOffset, this.yOffset+yOffset);
    }

    @Override
    public @NonNull Stream<IntPoint> allMapPositions() {
        return delegate.allMapPositions().map(p -> p.translate(-xOffset,-yOffset));
    }

    @Override
    public @NonNull Map<C> clearOffsets() {
        return delegate.clearOffsets();
    }

    @Override
    public boolean isOutside(int x, int y) {
        return delegate.isOutside(x+xOffset,y+yOffset);
    }

    @Override
    public @NonNull C createCell(@NonNull CellType cellType) {
        return delegate.createCell(cellType);
    }
}
