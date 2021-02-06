package perococco.jdgen.mapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.Size;

import java.util.Arrays;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Map {

    public static @NonNull Map create(@NonNull Size size) {
        final Cell[] cells = new Cell[size.height()*size.width()];
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

    private int toLinearCoordinate(int x, int y) {
        return x+y*size.width();
    }

    private void checkCoordinate(int x, int y) {
        if (x<0 || x >= size.width() || y < 0 || y >= size.height()) {
            throw new IllegalArgumentException("Coordinate are outside of the map x='"+x+"' y='"+y+"'");
        }
    }

}
