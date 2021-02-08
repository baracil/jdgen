package perococco.jdgen.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.Cell;
import perococco.jdgen.core.IntPoint;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public enum Pattern {
    UP(IntPoint::upperNeighbours, IntPoint::above, IntPoint::left, IntPoint::right),
    LEFT(IntPoint::leftNeighbours, IntPoint::left, IntPoint::above, IntPoint::below),
    RIGHT(IntPoint::rightNeighbours, IntPoint::right, IntPoint::above, IntPoint::below),
    BELOW(IntPoint::lowerNeighbours, IntPoint::below, IntPoint::left, IntPoint::right);


    private final @NonNull Function<IntPoint, Stream<IntPoint>> rowStream;
    private final @NonNull Function<IntPoint, IntPoint> centralProvider;
    private final @NonNull Function<IntPoint, IntPoint> side1Provider;
    private final @NonNull Function<IntPoint, IntPoint> side2Provider;


    public @NonNull boolean isDoorValid(@NonNull Map map, @NonNull IntPoint point) {
        final Function<IntPoint, CellType> cellTypeGetter = p -> map.getCellAt(p).getType();
        final var nbRooms = rowStream.apply(point)
                                     .map(cellTypeGetter)
                                     .filter(c -> c == CellType.ROOM_FLOOR)
                                     .count();
        final var nbRoomsValid = nbRooms >= 2L;

        if (nbRoomsValid) {
            return Stream.of(side1Provider, side2Provider)
                         .map(p -> p.apply(point))
                         .map(cellTypeGetter)
                         .allMatch(t -> t == CellType.EMPTY || t == CellType.DOOR);
        }
        return false;
    }

    public boolean shouldBeDoor(@NonNull Map map, @NonNull IntPoint point) {
        final Function<IntPoint, CellType> cellTypeGetter = p -> map.getCellAt(p).getType();
        final var has3Rooms = rowStream.apply(point)
                                       .map(cellTypeGetter)
                                       .allMatch(c -> c == CellType.ROOM_FLOOR);

        if (has3Rooms) {
            final var sides = Stream.of(side1Provider, side2Provider)
                                    .map(p -> p.apply(point))
                                    .map(cellTypeGetter)
                                    .filter(c -> c!=CellType.EMPTY)
                                    .collect(Collectors.toList());

            return sides.isEmpty() || (sides.size() == 1 && sides.get(0) == CellType.DOOR);
        }

        return false;
    }
}
