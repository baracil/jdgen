package perococco.jdgen.mapper;

import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.Couple;
import perococco.jdgen.core.Rectangle;
import perococco.jdgen.core.Room;
import perococco.jdgen.core.Size;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Mapper {

    public static @NonNull Map perform(
            @NonNull ImmutableList<Rectangle> rectangles,
            @NonNull ImmutableList<Room> rooms,
            @NonNull ImmutableList<Couple<Room>> corridors) {
        return new Mapper(rectangles, rooms, corridors).perform();
    }

    @NonNull ImmutableList<Rectangle> rectangles;
    @NonNull ImmutableList<Room> rooms;
    @NonNull ImmutableList<Couple<Room>> corridors;

    private int xOffset;
    private int yOffset;

    private Size size;

    private Map map;

    private @NonNull Map perform() {
        this.computeMapGeometry();
        this.createEmptyMap();
        this.fillCellsForMainRooms();
        return map;
    }

    private void computeMapGeometry() {
        final var xStat =rooms.stream().map(r -> r.geometry())
                              .flatMap(r -> IntStream.of(r.xc() - r.halfWidth(), r.xc() + r.halfWidth()).boxed())
                              .collect(Collectors.summarizingInt(i -> i));

        final var yStat =rooms.stream().map(r -> r.geometry())
                              .flatMap(r -> IntStream.of(r.yc() - r.halfHeight(), r.yc() + r.halfHeight()).boxed())
                              .collect(Collectors.summarizingInt(i -> i));

        xOffset = -xStat.getMin();
        yOffset = -yStat.getMin();
        size = new Size(xStat.getMax()-xStat.getMin()+1,yStat.getMax() - yStat.getMin()+1);
    }

    private void createEmptyMap() {
        this.map = Map.create(size);
    }

    private void fillCellsForMainRooms() {
        rooms.stream().map(r ->r.geometry())
             .forEach(this::fillCellsForRoom);
    }

    private void fillCellsForRoom(@NonNull Rectangle rectangle) {
        rectangle.streamPositions()
                 .forEach(p -> {
                     final var cellType = p.border()?CellType.WALL:CellType.FLOOR;
                     final var cell = new Cell(cellType);
                     map.setCellAt(cell,p.x()+xOffset,p.y()+yOffset);
                 });
    }
}
