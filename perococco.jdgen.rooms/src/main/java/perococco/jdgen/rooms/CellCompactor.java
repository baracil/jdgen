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
public class CellCompactor {

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

    private boolean performOneIteration() {
        boolean moved = false;
        Arrays.sort(sorted, Rectangle.DISTANCE_COMPARATOR);
        for (int i = 0, sortedLength = sorted.length; i < sortedLength; i++) {
            final var rectangle1 = sorted[i];
            final int nx = Math.abs(rectangle1.xc());
            final int ny = Math.abs(rectangle1.yc());
            if (Math.max(nx, ny) == 0) {
                continue;
            }

            var last = rectangle1;
            double dist = rectangle1.xc() * rectangle1.xc() + rectangle1.yc() * rectangle1.yc();

            for (int j = 0; j < ny; j++) {
                for (int k = 0; k < nx; k++) {
                    double dx = (rectangle1.xc() * k * 1.0) / nx;
                    double dy = (rectangle1.yc() * j * 1.0) / ny;

                    double d2 = dx * dx + dy * dy;

                    if (d2 >= dist) {
                        continue;
                    }

                    var rect = rectangle1.withPos(dx, dy);

                    if (collide(rect, sorted, i)) {
                        continue;
                    }
                    dist = d2;
                    last = rect;
                }
            }
            moved = moved || last != rectangle1;
            sorted[i] = last;
            observer.accept(ImmutableList.copyOf(sorted));
        }
        return moved;
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

    private double computeDelta(int p1, int hl1, int p2, int hl2) {
        if (p1 == 0) {
            return Double.NaN;
        }
        if (p1 < 0) {
            return -computeDelta(-p1, hl1, -p2, hl2);
        }
        assert p1 > 0;
        if (p2 > p1 || p2 < 0) {
            return Double.NaN;
        }

        var delta = (p1 - hl1) - (p2 + hl2 + 1);
        if (delta > 0) {
            return Double.NaN;
        }
        return -delta;
    }


}
