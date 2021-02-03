package perococco.jdgen.rooms;

import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.Rectangle;

import java.util.Arrays;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CellCompactor {

    public static ImmutableList<Rectangle> compact(@NonNull ImmutableList<Rectangle> input) {
        return new CellCompactor(input).compact();
    }


    private final @NonNull ImmutableList<Rectangle> input;

    private Rectangle[] sorted;

    private @NonNull ImmutableList<Rectangle> compact() {
        sorted = input.toArray(Rectangle[]::new);
        boolean moved = false;
        do {
            System.out.println("PLOP");
            moved = performOneIteration();
        } while (moved);
        System.out.println("DONE");

        return ImmutableList.copyOf(sorted);
    }

    private boolean performOneIteration() {
        boolean moved = false;
        Arrays.sort(sorted,Rectangle.DISTANCE_COMPARATOR.reversed());
        for (int i = 0, sortedLength = sorted.length; i < sortedLength; i++) {
            final var rectangle1 = sorted[i];
            final int nx = Math.abs(rectangle1.xc())-rectangle1.halfWidth();
            final int ny = Math.abs(rectangle1.yc())-rectangle1.halfHeight();
            final int n = Math.max(nx,ny);
            if (n == 0) {
                continue;
            }

            var last = rectangle1;

            for (int j = n-1; j >= 0; j--) {
                double dx = (rectangle1.xc()*j*1.0)/n;
                double dy = (rectangle1.yc()*j*1.0)/n;
                var rect = rectangle1.withPos(dx,dy);

                if (collide(rect,sorted,i)) {
                    break;
                }
                last = rect;
            }
            moved = moved || last != rectangle1;
            sorted[i] = last;
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
        if (p1<0) {
            return -computeDelta(-p1,hl1,-p2,hl2);
        }
        assert p1>0;
        if (p2>p1||p2<0) {
            return Double.NaN;
        }

        var delta = (p1-hl1)-(p2+hl2+1);
        if (delta>0) {
            return Double.NaN;
        }
        return -delta;
    }


}
