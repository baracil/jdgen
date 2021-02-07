package perococco.jdgen.mapper;

import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.*;

import java.util.Optional;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.IntStream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OneCorridorBuilder {


    public static void build(@NonNull MapperConfiguration configuration, @NonNull Map map, @NonNull Rectangle room1, @NonNull Rectangle room2) {
        new OneCorridorBuilder(configuration, map, room1, room2).build();
    }


    private final @NonNull MapperConfiguration configuration;
    private final @NonNull Map map;
    private final @NonNull Rectangle room1;
    private final @NonNull Rectangle room2;

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
        this.xOverlap = computeOverlap(Rectangle::computeXOverlap);
    }

    private void computeYOverlap() {
        this.yOverlap = computeOverlap(Rectangle::computeYOverlap);
    }

    private boolean roomsOverlapOnX() {
        return this.xOverlap != null;
    }

    private boolean roomsOverlapOnY() {
        return this.yOverlap != null;
    }

    private Overlap computeOverlap(@NonNull BiFunction<Rectangle, Rectangle, Optional<Overlap>> overlapComputer) {
        return overlapComputer.apply(room1, room2)
                              .flatMap(o -> o.shrinkBy(1))
                              .orElse(null);
    }

    private void buildVerticalCorridor() {
        assert xOverlap != null;
        final var xDoor = xOverlap.pickAtRandom(configuration.random());
        final var room1IsAboveRoom2 = room1.yc() > room2.yc();
        final var upperRoom = room1IsAboveRoom2 ? room1 : room2;
        final var lowerRoom = room1IsAboveRoom2 ? room2 : room1;

        IntStream.range(lowerRoom.yc() + lowerRoom.halfHeight(), upperRoom.yc() - upperRoom.halfHeight()+1)
                 .mapToObj(y -> new IntVector(xDoor, y))
                 .map(configuration::offsetToMapCoordinates)
                 .forEach(p -> map.setCellAt(new Cell(CellType.FLOOR), p));
    }


    private void buildHorizontalCorridor() {
        assert yOverlap != null;
        final var yDoor = yOverlap.pickAtRandom(configuration.random());
        final var room1IsRightOfRoom2 = room1.xc() > room2.xc();
        final var rightRoom = room1IsRightOfRoom2 ? room1 : room2;
        final var leftRoom = room1IsRightOfRoom2 ? room2 : room1;

        IntStream.range(leftRoom.xc() + leftRoom.halfWidth(), rightRoom.xc() - rightRoom.halfWidth()+1)
                 .mapToObj(x -> new IntVector(x, yDoor))
                 .map(configuration::offsetToMapCoordinates)
                 .forEach(p -> map.setCellAt(new Cell(CellType.FLOOR), p));

    }

}
