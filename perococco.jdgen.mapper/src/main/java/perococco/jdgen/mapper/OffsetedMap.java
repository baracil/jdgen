package perococco.jdgen.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.gen.generator.IntPoint;
import perococco.gen.generator.Map;
import perococco.gen.generator.MapCell;
import perococco.gen.generator.Size;

import java.util.stream.Stream;

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
    public @NonNull Map offsetMap(int xOffset, int yOffset) {
        return new OffsetedMap(delegate,this.xOffset+xOffset, this.yOffset+yOffset);
    }

    @Override
    public @NonNull Stream<IntPoint> allMapPositions() {
        return delegate.allMapPositions().map(p -> p.translate(-xOffset,-yOffset));
    }

    @Override
    public @NonNull Map clearOffsets() {
        return delegate.clearOffsets();
    }

    @Override
    public boolean isOutside(int x, int y) {
        return delegate.isOutside(x+xOffset,y+yOffset);
    }
}
