package perococco.jdgen.core;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Value
@EqualsAndHashCode(of = {"xc", "yc", "halfHeight", "halfWidth"})
public class Rectangle {

    public static final Comparator<Rectangle> DISTANCE_COMPARATOR = Comparator.comparingDouble(Rectangle::distance);

    int xc;
    int yc;

    int halfWidth;
    int halfHeight;

    double distance;

    public Rectangle(int xc, int yc, int halfWidth, int halfHeight) {
        this.xc = xc;
        this.yc = yc;
        this.halfWidth = halfWidth;
        this.halfHeight = halfHeight;
        this.distance = Math.sqrt(xc * xc + yc * yc);
    }

    public @NonNull Stream<RectanglePosition> streamPositions() {
        return IntStream.iterate(-halfWidth, w -> w <= halfWidth, w -> w + 1)
                        .mapToObj(w -> IntStream.iterate(-halfHeight, h -> h <= halfHeight, h -> h + 1)
                                                .mapToObj(h -> new IntVector(w, h)))
                        .flatMap(s -> s)
                        .map(this::toRectanglePosition);
    }

    private @NonNull RectanglePosition toRectanglePosition(@NonNull IntVector relativePosition) {
        final boolean border = Math.abs(relativePosition.x()) == halfWidth || Math.abs(relativePosition.y()) == halfHeight;
        return new RectanglePosition(relativePosition.x() + xc, relativePosition.y() + yc, border);

    }

    public @NonNull Optional<Overlap> computeXOverlap(@NonNull Rectangle other) {
        if (!overlapOnX(other)) {
            return Optional.empty();
        }
        if (xc > other.xc) {
            return other.computeXOverlap(this);
        }
        return Optional.of(new Overlap( other.xc - other.halfWidth,this.xc + this.halfWidth));
    }

    public @NonNull Optional<Overlap> computeYOverlap(@NonNull Rectangle other) {
        if (!overlapOnY(other)) {
            return Optional.empty();
        }
        if (yc > other.yc) {
            return other.computeYOverlap(this);
        }
        return Optional.of(new Overlap(other.yc - other.halfHeight, this.yc + this.halfHeight));

    }

    public boolean overlap(@NonNull Rectangle other) {
        return overlapOnX(other) && overlapOnY(other);
    }

    public boolean overlapOnX(@NonNull Rectangle other) {
        return Math.abs(other.xc - this.xc) <= (other.halfWidth + this.halfWidth);
    }

    public boolean overlapOnY(@NonNull Rectangle other) {
        return Math.abs(other.yc - this.yc) <= (other.halfHeight + this.halfHeight);
    }

    public int displacementToPutRightOf(@NonNull Rectangle reference) {
        return (reference.xc - this.xc) + (reference.halfWidth + this.halfWidth + 1);
    }

    public int displacementToPutLeftOf(@NonNull Rectangle reference) {
        return (reference.xc - this.xc) - (reference.halfWidth + this.halfWidth + 1);
    }

    public int displacementToPutAboveOf(@NonNull Rectangle reference) {
        return (reference.yc - this.yc) - (reference.halfHeight + this.halfHeight + 1);
    }

    public int displacementToPutBelowOf(@NonNull Rectangle reference) {
        return (reference.yc - this.yc) + (reference.halfHeight + this.halfHeight + 1);
    }


    public @NonNull Rectangle withPos(double posX, double posY) {
        return new Rectangle((int) Math.round(posX), (int) Math.round(posY), halfWidth, halfHeight);
    }

    public @NonNull Rectangle translate(@NonNull IntVector displacement) {
        return new Rectangle(xc + displacement.x(), yc + displacement.y(), halfWidth, halfHeight);
    }
}
