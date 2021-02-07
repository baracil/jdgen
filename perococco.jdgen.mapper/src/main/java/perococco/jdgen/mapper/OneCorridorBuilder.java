package perococco.jdgen.mapper;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.*;

import java.util.Optional;
import java.util.function.BiFunction;
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
        final var xDoor = xOverlap.pickAtRandom(parameters.getRandom());
        final var room1IsAboveRoom2 = room1.getYc() > room2.getYc();
        final var upperRoom = room1IsAboveRoom2 ? room1 : room2;
        final var lowerRoom = room1IsAboveRoom2 ? room2 : room1;

        IntStream.range(lowerRoom.getYc() + lowerRoom.getHalfHeight(), upperRoom.getYc() - upperRoom.getHalfHeight()+1)
                 .mapToObj(y -> new IntVector(xDoor, y))
                 .map(parameters::offsetToMapCoordinates)
                 .forEach(p -> parameters.getMap().setCellAt(new MapCell(CellType.FLOOR), p));
    }


    private void buildHorizontalCorridor() {
        assert yOverlap != null;
        final var yDoor = yOverlap.pickAtRandom(parameters.getRandom());
        final var room1IsRightOfRoom2 = room1.getXc() > room2.getXc();
        final var rightRoom = room1IsRightOfRoom2 ? room1 : room2;
        final var leftRoom = room1IsRightOfRoom2 ? room2 : room1;

        IntStream.range(leftRoom.getXc() + leftRoom.getHalfWidth(), rightRoom.getXc() - rightRoom.getHalfWidth()+1)
                 .mapToObj(x -> new IntVector(x, yDoor))
                 .map(parameters::offsetToMapCoordinates)
                 .forEach(p -> parameters.getMap().setCellAt(new MapCell(CellType.FLOOR), p));

    }

}
