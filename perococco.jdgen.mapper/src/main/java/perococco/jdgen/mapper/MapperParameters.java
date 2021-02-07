package perococco.jdgen.mapper;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.*;

import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MapperParameters {

    private final @NonNull JDGenConfiguration configuration;
    @Getter
    private final @NonNull Random random;
    private final @NonNull MapGeometry geometry;
    @Getter
    private final @NonNull Map map;
    private final @NonNull ImmutableList<Cell> cells;
    private final @NonNull ImmutableList<Room> rooms;
    private final @NonNull ImmutableList<Couple<Room>> corridors;


    public static MapperParameters create(
            @NonNull JDGenConfiguration configuration,
            @NonNull ImmutableList<Cell> cells,
            @NonNull ImmutableList<Room> rooms,
            @NonNull ImmutableList<Couple<Room>> corridors
    ) {
        final var cellAsRoom = rooms.stream().map(Room::getReference).collect(Collectors.toSet());
        final var cellsNotInRoom = cells.stream().filter(c -> !cellAsRoom.contains(c)).collect(ImmutableList.toImmutableList());
        final var geometry = GeometryComputer.compute(rooms);

        return new MapperParameters(
                configuration,
                Exec.with(new Random()).run(r -> r.setSeed(configuration.getSeed())),
                GeometryComputer.compute(rooms),
                Map.create(geometry.getSize())
                   .offsetMap(geometry.getXOffset(),geometry.getYOffset()),
                cellsNotInRoom,
                rooms,
                corridors
        );
    }


    public void forEachRooms(@NonNull Consumer<? super Room> roomConsumer) {
        rooms.forEach(roomConsumer);
    }

    public MapGeometry getGeometry() {
        return geometry;
    }

    public void forEachCorridors(@NonNull Consumer<? super Couple<Room>> corridorConsumer) {
        corridors.forEach(corridorConsumer);
    }

}
