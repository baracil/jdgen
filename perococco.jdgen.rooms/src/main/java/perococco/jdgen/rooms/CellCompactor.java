package perococco.jdgen.rooms;

import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.JDGenConfiguration;
import perococco.jdgen.core.Rectangle;

import java.util.Arrays;
import java.util.function.Consumer;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class CellCompactor {

    public static ImmutableList<Rectangle> compact(@NonNull JDGenConfiguration configuration,
                                                   @NonNull ImmutableList<Rectangle> cells, @NonNull Consumer<ImmutableList<Rectangle>> observer) {
        return new CellCompactor(configuration, cells, observer).compact();
    }


    private final JDGenConfiguration configuration;
    private final ImmutableList<Rectangle> cells;

    private final Consumer<ImmutableList<Rectangle>> observer;

    private Rectangle[] sorted;

    private @NonNull ImmutableList<Rectangle> compact() {
        sorted = cells.toArray(Rectangle[]::new);
        performOneIteration();
        return ImmutableList.copyOf(sorted);
    }

    private void performOneIteration() {
        Arrays.sort(sorted, Rectangle.DISTANCE_COMPARATOR);
        for (int i = 0, sortedLength = sorted.length; i < sortedLength; i++) {
            final var rectangle = sorted[i];


            final int nx = Math.abs(rectangle.xc());
            final int ny = Math.abs(rectangle.yc());
            if (Math.max(nx, ny) == 0) {
                continue;
            }

            final var sx = rectangle.xc() < 0 ? -1 : 1;
            final var sy = rectangle.yc() < 0 ? -1 : 1;

            var closestNotColliding = rectangle;

            for (int dx = 0; dx < nx; dx++) {
                final var x = dx*sx;
                for (int dy = 0; dy < ny; dy++) {
                    final var rect = rectangle.withPos(x,dy*sy);
                    if (rect.distance()>=closestNotColliding.distance()) {
                        continue;
                    }
                    if (!collide(rect, sorted, i)) {
                        closestNotColliding = rect;
                    }
                }
            }
            sorted[i] = closestNotColliding;
            observer.accept(ImmutableList.copyOf(sorted));
        }
    }

    private boolean collide(Rectangle rect, Rectangle[] sorted, int toSkip) {
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
