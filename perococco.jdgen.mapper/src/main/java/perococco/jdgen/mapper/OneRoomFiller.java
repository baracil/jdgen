package perococco.jdgen.mapper;


import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.api.CellType;
import perococco.jdgen.api.Cell;
import perococco.jdgen.core.RectangleGeometry;
import perococco.jdgen.core.RectanglePosition;

import java.util.function.Consumer;
import java.util.stream.Stream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OneRoomFiller<C extends Cell> {

    public static @NonNull Consumer<RectangleGeometry> createFiller(@NonNull MapperParameters parameters,
                                                                    @NonNull CellType cellType,
                                                                    boolean withWalls
    ) {
        return room -> fillRoom(parameters, cellType, withWalls, room);
    }

    public static <C extends Cell> void fillRoom(@NonNull MapperParameters<C> parameters,
                                @NonNull CellType cellType,
                                boolean withWalls,
                                @NonNull RectangleGeometry room) {
        new OneRoomFiller<>(parameters, cellType, withWalls, room).fill();
    }

    private final @NonNull MapperParameters<C> parameters;
    private final @NonNull CellType cellType;
    private final boolean withWalls;

    private final @NonNull RectangleGeometry room;

    private void fill() {
        streamRectanglePositions()
                .forEach(p -> {
                    final var cell = parameters.createCell(cellType);
                    parameters.getMap().setCellAtIfEmpty(cell, p);
                });
    }

    public Stream<RectanglePosition> streamRectanglePositions() {
        if (withWalls) {
            return room.streamPositions();
        } else {
            return room.streamPositionsWithoutBorders();
        }
    }

}
