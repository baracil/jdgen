package perococco.jdgen.mapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.IntPoint;
import perococco.jdgen.core.Size;

import java.util.Arrays;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ArrayMap implements Map {

    public static @NonNull ArrayMap create(@NonNull Size size) {
        final MapCell[] mapCells = new MapCell[size.getHeight()*size.getWidth()];
        Arrays.fill(mapCells, MapCell.empty());
        return new ArrayMap(size, mapCells);
    }

    @Getter
    private final @NonNull Size size;

    private final @NonNull MapCell[] mapCells;

    public MapCell getCellAt(int x, int y) {
        this.checkCoordinate(x,y);
        return mapCells[toLinearCoordinate(x, y)];
    }

    public void setCellAt(@NonNull MapCell mapCell, int x, int y) {
        this.checkCoordinate(x,y);
        mapCells[toLinearCoordinate(x, y)] = mapCell;
    }

    private int toLinearCoordinate(int x, int y) {
        return x+y*size.getWidth();
    }

    private void checkCoordinate(int x, int y) {
        if (x<0 || x >= size.getWidth() || y < 0 || y >= size.getHeight()) {
            throw new IllegalArgumentException("Coordinate are outside of the map x='"+x+"' y='"+y+"'");
        }
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
