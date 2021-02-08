package perococco.jdgen.mapper;


import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.api.CellType;
import perococco.jdgen.api.MapCell;
import perococco.jdgen.core.RectangleGeometry;
import perococco.jdgen.core.RectanglePosition;

import java.util.function.Consumer;
import java.util.stream.Stream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OneRoomFiller {

    public static @NonNull Consumer<RectangleGeometry> createFiller(@NonNull MapperParameters parameters,
                                                                    @NonNull CellType cellType,
                                                                    boolean withWalls
    ) {
        return room -> fillRoom(parameters, cellType, withWalls, room);
    }

    public static void fillRoom(@NonNull MapperParameters parameters,
                                @NonNull CellType cellType,
                                boolean withWalls,
                                @NonNull RectangleGeometry room) {
        new OneRoomFiller(parameters, cellType, withWalls, room).fill();
    }

    private final @NonNull MapperParameters parameters;
    private final @NonNull CellType cellType;
    private final boolean withWalls;

    private final @NonNull RectangleGeometry room;

    private void fill() {
        streamRectanglePositions()
                .forEach(p -> {
                    final var cell = new MapCell(cellType);
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
