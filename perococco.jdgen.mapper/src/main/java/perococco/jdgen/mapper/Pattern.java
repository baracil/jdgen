package perococco.jdgen.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.api.CellType;
import perococco.jdgen.api.Position;
import perococco.jdgen.api.Map;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public enum Pattern {
    UP(Position::upperNeighbours, Position::left, Position::right),
    LEFT(Position::leftNeighbours, Position::above, Position::below),
    RIGHT(Position::rightNeighbours, Position::above, Position::below),
    BELOW(Position::lowerNeighbours, Position::left, Position::right);


    private final @NonNull Function<Position, Stream<Position>> rowStream;
    private final @NonNull Function<Position, Position> side1Provider;
    private final @NonNull Function<Position, Position> side2Provider;


    public @NonNull boolean isDoorValid(@NonNull Map map, @NonNull Position point) {
        final Function<Position, CellType> cellTypeGetter = p -> map.getCellAt(p).getType();
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

    public boolean shouldBeDoor(@NonNull Map map, @NonNull Position point) {
        final Function<Position, CellType> cellTypeGetter = p -> map.getCellAt(p).getType();
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
