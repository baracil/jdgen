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

    public static final Comparator<Rectangle> DISTANCE_COMPARATOR = Comparator.comparingDouble(Rectangle::getDistance);

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
        final int width = halfWidth * 2 + 1;
        final int height = halfHeight * 2 + 1;
        return IntStream.range(0, width * height)
                        .mapToObj(i -> new IntVector((i % width) - halfWidth, (i / width) - halfHeight))
                        .map(this::toRectanglePosition);
    }

    private @NonNull RectanglePosition toRectanglePosition(@NonNull IntVector relativePosition) {
        final boolean border = Math.abs(relativePosition.getX()) == halfWidth || Math.abs(relativePosition.getY()) == halfHeight;
        return new RectanglePosition(relativePosition.getX() + xc, relativePosition.getY() + yc, border);

    }

    public @NonNull Optional<Overlap> computeXOverlap(@NonNull Rectangle other) {
        return computeOverlap(other, X_AXIS_GETTER);
    }

    public @NonNull Optional<Overlap> computeYOverlap(@NonNull Rectangle other) {
        return computeOverlap(other, Y_AXIS_GETTER);
    }

    private @NonNull Optional<Overlap> computeOverlap(@NonNull Rectangle other, @NonNull AxisGetter axisGetter) {
        if (axisGetter.getCenter(other) < axisGetter.getCenter(this)) {
            return other.computeOverlap(this, axisGetter);
        }
        if (!overlapOnAxis(other, axisGetter)) {
            return Optional.empty();
        }
        return Optional.of(new Overlap(axisGetter.getLowerBound(other), axisGetter.getUpperBound(this)));
    }

    public boolean overlap(@NonNull Rectangle other) {
        return overlapOnX(other) && overlapOnY(other);
    }

    public boolean overlapOnX(@NonNull Rectangle other) {
        return overlapOnAxis(other, X_AXIS_GETTER);
    }

    public boolean overlapOnY(@NonNull Rectangle other) {
        return overlapOnAxis(other, Y_AXIS_GETTER);
    }

    private boolean overlapOnAxis(@NonNull Rectangle other, @NonNull Rectangle.AxisGetter axisGetter) {
        return Math.abs(axisGetter.getCenter(other) - axisGetter.getCenter(this)) <= (axisGetter.getHalfLength(other) + axisGetter.getHalfLength(this));
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
        return new Rectangle(xc + displacement.getX(), yc + displacement.getY(), halfWidth, halfHeight);
    }


    public static final AxisGetter X_AXIS_GETTER = new AxisGetter() {
        @Override
        public int getCenter(@NonNull Rectangle rectangle) {
            return rectangle.getXc();
        }

        @Override
        public int getHalfLength(@NonNull Rectangle rectangle) {
            return rectangle.getHalfWidth();
        }
    };

    public static final AxisGetter Y_AXIS_GETTER = new AxisGetter() {
        @Override
        public int getCenter(@NonNull Rectangle rectangle) {
            return rectangle.getYc();
        }

        @Override
        public int getHalfLength(@NonNull Rectangle rectangle) {
            return rectangle.getHalfHeight();
        }


    };

    public interface AxisGetter {
        int getCenter(@NonNull Rectangle rectangle);

        int getHalfLength(@NonNull Rectangle rectangle);

        default int getLowerBound(@NonNull Rectangle rectangle) {
            return getCenter(rectangle) - getHalfLength(rectangle);
        }

        default int getUpperBound(@NonNull Rectangle rectangle) {
            return getCenter(rectangle) + getHalfLength(rectangle);
        }
    }


}
