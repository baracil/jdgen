package perococco.jdgen.core._private;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import perococco.jdgen.core.*;

import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Value
@EqualsAndHashCode(of = {"xc", "yc", "halfHeight", "halfWidth"})
public class SimpleRectangle implements Rectangle {

    int xc;
    int yc;

    int halfWidth;
    int halfHeight;

    @Getter
    double distance;

    public SimpleRectangle(int xc, int yc, int halfWidth, int halfHeight) {
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

    public @NonNull Optional<Overlap> computeXOverlap(@NonNull RectangleGeometry other) {
        return computeOverlap(other, X_AXIS_GETTER);
    }

    public @NonNull Optional<Overlap> computeYOverlap(@NonNull RectangleGeometry other) {
        return computeOverlap(other, Y_AXIS_GETTER);
    }

    public @NonNull Optional<Overlap> computeOverlap(@NonNull RectangleGeometry other, @NonNull RectangleGeometry.AxisOperations axisOperations) {
        if (axisOperations.getCenter(other) < axisOperations.getCenter(this)) {
            return other.computeOverlap(this, axisOperations);
        }
        if (!overlapOnAxis(other, axisOperations)) {
            return Optional.empty();
        }
        return Optional.of(new Overlap(axisOperations.getLowerBound(other), axisOperations.getUpperBound(this)));
    }

    public boolean overlapOnX(@NonNull RectangleGeometry other) {
        return overlapOnAxis(other, X_AXIS_GETTER);
    }

    public boolean overlapOnY(@NonNull RectangleGeometry other) {
        return overlapOnAxis(other, Y_AXIS_GETTER);
    }

    private boolean overlapOnAxis(@NonNull RectangleGeometry other, @NonNull RectangleGeometry.AxisOperations axisOperations) {
        return Math.abs(axisOperations.getCenter(other) - axisOperations.getCenter(this)) <= (axisOperations.getHalfLength(other) + axisOperations.getHalfLength(this));
    }

    public int displacementToPutRightOf(@NonNull Rectangle reference) {
        return (reference.getXc() - this.xc) + (reference.getHalfWidth() + this.halfWidth + 1);
    }

    public int displacementToPutLeftOf(@NonNull Rectangle reference) {
        return (reference.getXc() - this.xc) - (reference.getHalfWidth() + this.halfWidth + 1);
    }

    public int displacementToPutAboveOf(@NonNull Rectangle reference) {
        return (reference.getYc() - this.yc) - (reference.getHalfHeight() + this.halfHeight + 1);
    }

    public int displacementToPutBelowOf(@NonNull Rectangle reference) {
        return (reference.getYc() - this.yc) + (reference.getHalfHeight() + this.halfHeight + 1);
    }


    public @NonNull Rectangle withPos(double posX, double posY) {
        return new SimpleRectangle((int) Math.round(posX), (int) Math.round(posY), halfWidth, halfHeight);
    }

    public @NonNull Rectangle translate(@NonNull IntVector displacement) {
        return new SimpleRectangle(xc + displacement.getX(), yc + displacement.getY(), halfWidth, halfHeight);
    }

}
