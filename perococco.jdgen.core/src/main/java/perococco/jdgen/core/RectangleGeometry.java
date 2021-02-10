package perococco.jdgen.core;

import lombok.NonNull;
import perococco.jdgen.api.Position;

import java.util.Comparator;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

public interface RectangleGeometry {


    Comparator<RectangleGeometry> DISTANCE_COMPARATOR = Comparator.comparingDouble(RectangleGeometry::getDistance);

    int getXc();

    int getYc();

    int getHalfWidth();

    int getHalfHeight();

    default int getWidth() {
        return getHalfWidth()*2+1;
    }

    default int getHeight() {
        return getHalfHeight()*2+1;
    }

    default int pickPositionOnHeightWithoutBorder(@NonNull Random random) {
        return getYc()+random.nextInt(getHeight()-2)-getHalfHeight()+1;
    }

    default int pickPositionOnWidthWithoutBorder(@NonNull Random random) {
        return getXc()+random.nextInt(getWidth()-2)-getHalfWidth()+1;
    }

    double getDistance();

    @NonNull Stream<Position> streamPositions();

    @NonNull Stream<Position> streamPositionsWithoutBorders();

    @NonNull Optional<Overlap> computeOverlap(@NonNull RectangleGeometry rectangle, @NonNull RectangleGeometry.AxisOperations axisOperations);

    default boolean overlap(@NonNull RectangleGeometry other) {
        return overlapOnX(other) && overlapOnY(other);
    }

    boolean overlapOnX(@NonNull RectangleGeometry other);

    boolean overlapOnY(@NonNull RectangleGeometry other);



    AxisOperations X_AXIS_GETTER = new AxisOperations() {
        @Override
        public int getCenter(@NonNull RectangleGeometry rectangle) {
            return rectangle.getXc();
        }

        @Override
        public int getHalfLength(@NonNull RectangleGeometry rectangle) {
            return rectangle.getHalfWidth();
        }

        @Override
        public int pickPositionOnSizeWithoutBorder(@NonNull RectangleGeometry rectangle, @NonNull Random random) {
            return rectangle.pickPositionOnWidthWithoutBorder(random);
        }
    };

    AxisOperations Y_AXIS_GETTER = new AxisOperations() {
        @Override
        public int getCenter(@NonNull RectangleGeometry rectangle) {
            return rectangle.getYc();
        }

        @Override
        public int getHalfLength(@NonNull RectangleGeometry rectangle) {
            return rectangle.getHalfHeight();
        }

        @Override
        public int pickPositionOnSizeWithoutBorder(@NonNull RectangleGeometry rectangle, @NonNull Random random) {
            return rectangle.pickPositionOnHeightWithoutBorder(random);
        }
    };


    interface AxisOperations {
        int getCenter(@NonNull RectangleGeometry rectangle);

        int getHalfLength(@NonNull RectangleGeometry rectangle);

        default int getLowerBound(@NonNull RectangleGeometry rectangle) {
            return getCenter(rectangle) - getHalfLength(rectangle);
        }

        default int getUpperBound(@NonNull RectangleGeometry rectangle) {
            return getCenter(rectangle) + getHalfLength(rectangle);
        }

        default @NonNull Optional<Overlap> computeOverlap(@NonNull RectangleGeometry geometry1, @NonNull RectangleGeometry geometry2) {
            return geometry1.computeOverlap(geometry2,this);
        }

        int pickPositionOnSizeWithoutBorder(@NonNull RectangleGeometry rectangle, @NonNull Random random);
    }

}
