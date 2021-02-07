package perococco.jdgen.mapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.IntPoint;
import perococco.jdgen.core.Size;

import java.util.Arrays;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Map {

    public static @NonNull Map create(@NonNull Size size) {
        final MapCell[] mapCells = new MapCell[size.getHeight()*size.getWidth()];
        Arrays.fill(mapCells, MapCell.empty());
        return new Map(size, mapCells);
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

    public void setCellAt(@NonNull MapCell mapCell, IntPoint position) {
        this.setCellAt(mapCell, position.getX(), position.getY());
    }

    private int toLinearCoordinate(int x, int y) {
        return x+y*size.getWidth();
    }

    private void checkCoordinate(int x, int y) {
        if (x<0 || x >= size.getWidth() || y < 0 || y >= size.getHeight()) {
            throw new IllegalArgumentException("Coordinate are outside of the map x='"+x+"' y='"+y+"'");
        }
    }

}
