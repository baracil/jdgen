package perococco.jdgen.core._private;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import perococco.jdgen.api.Position;
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

    public @NonNull Stream<Position> streamPositions() {
        final int width = halfWidth * 2 + 1;
        final int height = halfHeight * 2 + 1;
        final int dx = xc-halfWidth;
        final int dy = yc-halfHeight;
        return IntStream.range(0, width * height)
                        .mapToObj(i -> Position.at((i % width) + dx, (i / width) + dy));
    }

    @Override
    public @NonNull Stream<Position> streamPositionsWithoutBorders() {
        return streamPositions().filter(p -> !isOnBorder(p));
    }

    private @NonNull boolean isOnBorder(@NonNull Position position) {
        return Math.abs(position.getX()-xc) == halfWidth || Math.abs(position.getY()-yc) == halfHeight;
    }

    public @NonNull Optional<Overlap> computeOverlap(@NonNull RectangleGeometry other, @NonNull RectangleGeometry.AxisOperations axisOperations) {
        if (!overlapOnAxis(other, axisOperations)) {
            return Optional.empty();
        }

        final var lower = Math.max(axisOperations.getLowerBound(other), axisOperations.getLowerBound(this));
        final var upper = Math.min(axisOperations.getUpperBound(other), axisOperations.getUpperBound(this));

        return Optional.of(new Overlap(lower,upper));
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

    public @NonNull Rectangle translate(@NonNull Vector displacement) {
        return new SimpleRectangle(xc + displacement.getX(), yc + displacement.getY(), halfWidth, halfHeight);
    }

}
