package perococco.jdgen.api;

import lombok.NonNull;
import lombok.Value;

import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Value
public class Position {

    public static Position at(int x, int y) {
        return new Position(x, y);
    }

    int x;
    int y;

    public Stream<Position> neighbours() {
        return NEIGHBOURS_OFFSET.stream().map(p -> p.translate(x,y));
    }

    public @NonNull Position translate(int xoffset, int yoffset) {
        return new Position(x + xoffset, y + yoffset);
    }

    public @NonNull Stream<Position> leftNeighbours() {
        final var factory = deltaYFactory(x - 1, y);
        return IntStream.rangeClosed(-1, +1).mapToObj(factory);
    }

    public @NonNull Stream<Position> rightNeighbours() {
        final var factory = deltaYFactory(x + 1, y);
        return IntStream.rangeClosed(-1, +1).mapToObj(factory);
    }

    public @NonNull Stream<Position> upperNeighbours() {
        final var factory = deltaXFactory(x, y + 1);
        return IntStream.rangeClosed(-1, +1).mapToObj(factory);
    }

    public @NonNull Stream<Position> lowerNeighbours() {
        final var factory = deltaXFactory(x, y - 1);
        return IntStream.rangeClosed(-1, +1).mapToObj(factory);
    }

    public @NonNull Position left() {
        return translate(-1, 0);
    }

    public @NonNull Position right() {
        return translate(+1, 0);
    }

    public @NonNull Position above() {
        return translate(0, +1);
    }

    public @NonNull Position below() {
        return translate(0, -1);
    }


    private @NonNull IntFunction<Position> deltaYFactory(int x, int yOrigin) {
        return deltaY -> new Position(x, yOrigin + deltaY);
    }

    private @NonNull IntFunction<Position> deltaXFactory(int xOrigin, int y) {
        return deltaX -> new Position(xOrigin + deltaX, y);
    }


    private static final List<Position> NEIGHBOURS_OFFSET = List.of(
            Position.at(-1, -1),
            Position.at(-1,  0),
            Position.at(-1, +1),
            Position.at( 0, -1),
            Position.at( 0, +1),
            Position.at(+1, -1),
            Position.at(+1,  0),
            Position.at(+1, +1)
    );
}
