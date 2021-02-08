package perococco.jdgen.mapper;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.gen.generator.CellType;
import perococco.gen.generator.IntPoint;
import perococco.gen.generator.MapCell;
import perococco.jdgen.core.*;

import java.util.function.IntFunction;

import static perococco.jdgen.core.RectangleGeometry.X_AXIS_GETTER;
import static perococco.jdgen.core.RectangleGeometry.Y_AXIS_GETTER;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OneCorridorBuilder {


    public static void build(@NonNull MapperParameters parameters, @NonNull Room room1, @NonNull Room room2) {
        if (room1.getYc()>=room2.getYc()) {
            new OneCorridorBuilder(parameters, room1, room2).build();
        }
        else {
            new OneCorridorBuilder(parameters, room2, room1).build();
        }
    }

    private final @NonNull MapperParameters parameters;
    private final @NonNull Room upperRoom;
    private final @NonNull Room lowerRoom;

    private Overlap xOverlap;
    private Overlap yOverlap;

    private void build() {
        this.assertCorrectRoomOrder();
        this.computeXOverlap();
        this.computeYOverlap();

        if (roomsOverlapOnX()) {
            this.buildVerticalCorridor();
        } else if (roomsOverlapOnY()) {
            this.buildHorizontalCorridor();
        } else {
            this.buildLShapeCorridor();
        }
    }

    private void assertCorrectRoomOrder() {
        assert upperRoom.getYc()>=lowerRoom.getYc();
    }

    private void computeXOverlap() {
        this.xOverlap = computeOverlap(X_AXIS_GETTER);
    }

    private void computeYOverlap() {
        this.yOverlap = computeOverlap(Y_AXIS_GETTER);
    }

    private boolean roomsOverlapOnX() {
        return this.xOverlap != null;
    }

    private boolean roomsOverlapOnY() {
        return this.yOverlap != null;
    }

    private Overlap computeOverlap(@NonNull RectangleGeometry.AxisOperations operations) {
        return operations.computeOverlap(upperRoom, lowerRoom)
                              .flatMap(o -> o.shrinkBy(1))
                              .orElse(null);
    }

    private void buildVerticalCorridor() {
        assert xOverlap != null;
        buildLinearCorridor(xOverlap,x -> y -> new IntVector(x,y), Y_AXIS_GETTER);
    }

    private void buildHorizontalCorridor() {
        assert yOverlap != null;
        buildLinearCorridor(yOverlap,y -> x -> new IntVector(x,y), X_AXIS_GETTER);
    }

    private void buildLinearCorridor(@NonNull Overlap overlap,
                                     @NonNull IntFunction<IntFunction<IntVector>> doorFactory,
                                     @NonNull RectangleGeometry.AxisOperations op) {
        final var doorPosition = overlap.pickAtRandom(parameters.getRandom());
        final IntFunction<IntVector> corridorPoint = doorFactory.apply(doorPosition);


        final var room1IsHigher = op.getCenter(upperRoom) > op.getCenter(lowerRoom);
        final var upperRoom = room1IsHigher ? this.upperRoom : lowerRoom;
        final var lowerRoom = room1IsHigher ? this.lowerRoom : this.upperRoom;

        final var start = corridorPoint.apply(op.getUpperBound(lowerRoom));
        final var end = corridorPoint.apply(op.getLowerBound(upperRoom));


        fillLine(start,end);
        parameters.getMap().setCellAt(new MapCell(CellType.DOOR), start);
        parameters.getMap().setCellAt(new MapCell(CellType.DOOR), end);

    }

    private void buildLShapeCorridor() {
        if (upperRoom.getXc()>lowerRoom.getXc()) {
            generateCorridorForCase1();
        } else {
            generateCorridorForCase2();
        }
    }

    private void generateCorridorForCase1() {
        final Runnable generator = parameters.getRandom().nextBoolean()
                ?this::generateCorridorForGaucheHaut
                :this::generateCorridorForBasDroite;
        generator.run();
    }

    private void generateCorridorForCase2() {
        final Runnable generator = parameters.getRandom().nextBoolean()
                ?this::generateCorridorForDroiteHaut
                :this::generateCorridorForBasGauche;
        generator.run();
    }

    private void fillCorridor(@NonNull IntPoint start, @NonNull IntPoint end, @NonNull IntPoint middle) {
        fillLine(start,middle);
        fillLine(end,middle);
        parameters.getMap().setCellAt(new MapCell(CellType.DOOR),start);
        parameters.getMap().setCellAt(new MapCell(CellType.DOOR),end);
    }

    private void generateCorridorForGaucheHaut() {
        var xs = X_AXIS_GETTER.getLowerBound(upperRoom);
        var ys = Y_AXIS_GETTER.pickPositionOnSizeWithoutBorder(upperRoom,parameters.getRandom());

        var xe = X_AXIS_GETTER.pickPositionOnSizeWithoutBorder(lowerRoom,parameters.getRandom());
        var ye = Y_AXIS_GETTER.getUpperBound(lowerRoom);

        final var start = new IntVector(xs,ys);
        final var end = new IntVector(xe,ye);
        final var middle = new IntVector(xe,ys);

        fillCorridor(start,end,middle);
    }
    private void generateCorridorForDroiteHaut() {
        var xs = X_AXIS_GETTER.getUpperBound(upperRoom);
        var ys = Y_AXIS_GETTER.pickPositionOnSizeWithoutBorder(upperRoom, parameters.getRandom());

        var xe = X_AXIS_GETTER.pickPositionOnSizeWithoutBorder(lowerRoom,parameters.getRandom());
        var ye = Y_AXIS_GETTER.getUpperBound(lowerRoom);

        final var start = new IntVector(xs,ys);
        final var end = new IntVector(xe,ye);
        final var middle = new IntVector(xe,ys);

        fillCorridor(start,end,middle);
    }

    private void generateCorridorForBasDroite() {
        var xs = X_AXIS_GETTER.pickPositionOnSizeWithoutBorder(upperRoom, parameters.getRandom());
        var ys = Y_AXIS_GETTER.getLowerBound(upperRoom);

        var xe = X_AXIS_GETTER.getUpperBound(lowerRoom);
        var ye = Y_AXIS_GETTER.pickPositionOnSizeWithoutBorder(lowerRoom, parameters.getRandom());

        final var start = new IntVector(xs,ys);
        final var end = new IntVector(xe,ye);
        final var middle = new IntVector(xs,ye);

        fillCorridor(start,end,middle);
    }
    private void generateCorridorForBasGauche() {

        var xs = X_AXIS_GETTER.pickPositionOnSizeWithoutBorder(upperRoom, parameters.getRandom());
        var ys = Y_AXIS_GETTER.getLowerBound(upperRoom);

        var xe = X_AXIS_GETTER.getLowerBound(lowerRoom);
        var ye = Y_AXIS_GETTER.pickPositionOnSizeWithoutBorder(lowerRoom,parameters.getRandom());

        final var start = new IntVector(xs,ys);
        final var end = new IntVector(xe,ye);
        final var middle = new IntVector(xs,ye);

        fillCorridor(start,end,middle);
    }



    private void fillLine(@NonNull IntPoint start, @NonNull IntPoint end) {
        if (start.getX() == end.getX()) {
            final var ys = Math.min(start.getY(), end.getY());
            final var ye = Math.max(start.getY(), end.getY());
            for (int y = ys; y <= ye ; y++) {
                parameters.getMap().setCellAtIfEmpty(new MapCell(CellType.CORRIDOR_FLOOR),start.getX(),y);
            }
        }
        else if (start.getY() == end.getY()) {
            final var xs = Math.min(start.getX(), end.getX());
            final var xe = Math.max(start.getX(), end.getX());
            for (int x = xs; x <= xe ; x++) {
                parameters.getMap().setCellAtIfEmpty(new MapCell(CellType.CORRIDOR_FLOOR),x,start.getY());
            }

        }

    }

}
