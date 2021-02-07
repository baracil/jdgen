package perococco.jdgen.mapper;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.*;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OneCorridorBuilder {


    public static void build(@NonNull MapperParameters parameters, @NonNull Room room1, @NonNull Room room2) {
        new OneCorridorBuilder(parameters, room1, room2).build();
    }


    private final @NonNull MapperParameters parameters;
    private final @NonNull Room room1;
    private final @NonNull Room room2;

    private Overlap xOverlap;
    private Overlap yOverlap;

    private void build() {
        this.computeXOverlap();
        this.computeYOverlap();

        if (roomsOverlapOnX()) {
            this.buildVerticalCorridor();
        } else if (roomsOverlapOnY()) {
            this.buildHorizontalCorridor();
        }
    }

    private void computeXOverlap() {
        this.xOverlap = computeOverlap(RectangleGeometry.X_AXIS_GETTER);
    }

    private void computeYOverlap() {
        this.yOverlap = computeOverlap(RectangleGeometry.Y_AXIS_GETTER);
    }

    private boolean roomsOverlapOnX() {
        return this.xOverlap != null;
    }

    private boolean roomsOverlapOnY() {
        return this.yOverlap != null;
    }

    private Overlap computeOverlap(@NonNull RectangleGeometry.AxisOperations operations) {
        return operations.computeOverlap(room1, room2)
                              .flatMap(o -> o.shrinkBy(1))
                              .orElse(null);
    }

    private void buildVerticalCorridor() {
        assert xOverlap != null;
        buildLinearCorridor(xOverlap,x -> y -> new IntVector(x,y), RectangleGeometry.Y_AXIS_GETTER);
    }

    private void buildHorizontalCorridor() {
        assert yOverlap != null;
        buildLinearCorridor(yOverlap,y -> x -> new IntVector(x,y), RectangleGeometry.X_AXIS_GETTER);
    }

    private void buildLinearCorridor(@NonNull Overlap overlap,
                                     @NonNull IntFunction<IntFunction<IntVector>> doorFactory,
                                     @NonNull RectangleGeometry.AxisOperations op) {
        final var doorPosition = overlap.pickAtRandom(parameters.getRandom());
        final IntFunction<IntVector> corridorPoint = doorFactory.apply(doorPosition);


        final var room1IsHigher = op.getCenter(room1) > op.getCenter(room2);
        final var upperRoom = room1IsHigher ? room1 : room2;
        final var lowerRoom = room1IsHigher ? room2 : room1;

        IntStream.rangeClosed(op.getUpperBound(lowerRoom), op.getLowerBound(upperRoom))
                 .mapToObj(corridorPoint)
                 .map(parameters::offsetToMapCoordinates)
                 .forEach(p -> parameters.getMap().setCellAt(new MapCell(CellType.FLOOR), p));
    }



}
