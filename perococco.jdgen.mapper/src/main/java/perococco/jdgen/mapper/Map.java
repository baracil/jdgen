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
        final Cell[] cells = new Cell[size.getHeight()*size.getWidth()];
        Arrays.fill(cells,Cell.empty());
        return new Map(size,cells);
    }

    @Getter
    private final @NonNull Size size;

    private final @NonNull Cell[] cells;

    public Cell getCellAt(int x, int y) {
        this.checkCoordinate(x,y);
        return cells[toLinearCoordinate(x,y)];
    }

    public void setCellAt(@NonNull Cell cell, int x, int y) {
        this.checkCoordinate(x,y);
        cells[toLinearCoordinate(x,y)] = cell;
    }

    public void setCellAt(@NonNull Cell cell, IntPoint position) {
        this.setCellAt(cell, position.getX(), position.getY());
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
