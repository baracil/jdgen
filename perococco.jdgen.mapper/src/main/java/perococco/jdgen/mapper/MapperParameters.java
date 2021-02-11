package perococco.jdgen.mapper;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.api.*;
import perococco.jdgen.core.*;
import perococco.jdgen.core.Cell;

import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class MapperParameters<C extends perococco.jdgen.api.Cell> {

    private final @NonNull JDGenConfiguration configuration;
    private final @NonNull CellFactory<C> cellFactory;
    @Getter
    private final @NonNull Random random;
    @Getter
    private final @NonNull Map<C> map;
    private final @NonNull ImmutableList<Cell> cells;
    private final @NonNull ImmutableList<Room> rooms;
    private final @NonNull ImmutableList<Couple<Room>> corridors;


    public @NonNull Stream<Cell> cellStream() {
        return cells.stream();
    }

    public void forEachRooms(@NonNull Consumer<? super Room> roomConsumer) {
        rooms.forEach(roomConsumer);
    }

    public void forEachCorridors(@NonNull Consumer<? super Couple<Room>> corridorConsumer) {
        corridors.forEach(corridorConsumer);
    }

    public void setCellTypeAt(@NonNull CellType cellType,@NonNull Position position) {
        map.setCellAt(createCell(cellType),position);
    }

    public void setCellTypeAtIfEmpty(CellType cellType, @NonNull Position position) {
        map.setCellAtIfEmpty(createCell(cellType),position);
    }



    @NonNull
    public C createCell(@NonNull CellType cellType) {
        return cellFactory.createCell(cellType);
    }

    public static <C extends perococco.jdgen.api.Cell> MapperParameters<C> create(
            @NonNull JDGenConfiguration configuration,
            @NonNull CellFactory<C> cellFactory,
            @NonNull ImmutableList<Cell> cells,
            @NonNull ImmutableList<Room> rooms,
            @NonNull ImmutableList<Couple<Room>> corridors
    ) {
        final var cellAsRoom = rooms.stream().map(Room::getReference).collect(Collectors.toSet());
        final var cellsNotInRoom = cells.stream().filter(c -> !cellAsRoom.contains(c)).collect(ImmutableList.toImmutableList());
        final var geometry = GeometryComputer.compute(rooms);

        final var dx = geometry.getXOffset()+2;
        final var dy = geometry.getYOffset()+2;

        return new MapperParameters<C>(
                configuration,
                cellFactory,
                Exec.with(new Random()).run(r -> r.setSeed(configuration.getSeed())),
                ArrayMap.create(geometry.getSize().addMargin(2), cellFactory)
                        .setTransformation(Transformation.offset(dx,dy)),
                cellsNotInRoom,
                rooms,
                corridors
        );
    }

    public @NonNull CellType getCellTypeAt(@NonNull Position position) {
        if (map.isOutside(position)) {
            return CellType.EMPTY;
        }
        return map.getCellAt(position).getType();
    }
}
