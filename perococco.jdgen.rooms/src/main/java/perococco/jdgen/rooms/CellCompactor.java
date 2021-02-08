package perococco.jdgen.rooms;

import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.Cell;
import perococco.jdgen.core.RectangleGeometry;

import java.util.Arrays;
import java.util.function.Consumer;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class CellCompactor {

    public static ImmutableList<Cell> compact(@NonNull ImmutableList<Cell> cells,
                                              @NonNull Consumer<ImmutableList<Cell>> observer) {
        return new CellCompactor(cells, observer).compact();
    }

    public static ImmutableList<Cell> compact(@NonNull ImmutableList<Cell> cells) {
        return compact(cells, l -> {});
    }


    private final ImmutableList<Cell> mapCells;

    private final Consumer<ImmutableList<Cell>> observer;

    private Cell[] sorted;

    private @NonNull ImmutableList<Cell> compact() {
        sorted = mapCells.toArray(Cell[]::new);
        performOneIteration();
        return ImmutableList.copyOf(sorted);
    }

    private void performOneIteration() {
        Arrays.sort(sorted, RectangleGeometry.DISTANCE_COMPARATOR);
        for (int i = 0, sortedLength = sorted.length; i < sortedLength; i++) {
            final var cell = sorted[i];


            final int nx = Math.abs(cell.getXc());
            final int ny = Math.abs(cell.getYc());
            if (Math.max(nx, ny) == 0) {
                continue;
            }

            final var sx = cell.getXc() < 0 ? -1 : 1;
            final var sy = cell.getYc() < 0 ? -1 : 1;

            var closestNotColliding = cell;

            for (int dx = 0; dx < nx; dx++) {
                final var x = dx*sx;
                for (int dy = 0; dy < ny; dy++) {
                    final var movedCell = cell.withPos(x,dy*sy);
                    if (movedCell.getDistance()>=closestNotColliding.getDistance()) {
                        continue;
                    }
                    if (!collide(movedCell, sorted, i)) {
                        closestNotColliding = movedCell;
                    }
                }
            }
            sorted[i] = closestNotColliding;
            observer.accept(ImmutableList.copyOf(sorted));
        }
    }

    private boolean collide(RectangleGeometry rect, RectangleGeometry[] sorted, int toSkip) {
        for (int i = 0; i < sorted.length; i++) {
            if (i == toSkip) {
                continue;
            }
            if (rect.overlap(sorted[i])) {
                return true;
            }
        }
        return false;
    }
}
