package perococco.jdgen.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.IntPoint;
import perococco.jdgen.core.Size;

@RequiredArgsConstructor
public class OffsetedMap implements Map {

    private final @NonNull Map delegate;
    private final int xOffset;
    private final int yOffset;

    @Override
    public @NonNull Size getSize() {
        return delegate.getSize();
    }

    @Override
    public MapCell getCellAt(int x, int y) {
        return delegate.getCellAt(x+xOffset,y+yOffset);
    }

    @Override
    public void setCellAt(@NonNull MapCell mapCell, int x, int y) {
        delegate.setCellAt(mapCell, x+xOffset,y+yOffset);
    }

    @Override
    public void setCellAt(@NonNull MapCell mapCell, IntPoint position) {
        setCellAt(mapCell,position.getX(),position.getY());
    }

    @Override
    public @NonNull Map offsetMap(int xOffset, int yOffset) {
        return new OffsetedMap(delegate,this.xOffset+xOffset, this.yOffset+yOffset);
    }

    @Override
    public @NonNull Map clearOffsets() {
        return delegate.clearOffsets();
    }
}
