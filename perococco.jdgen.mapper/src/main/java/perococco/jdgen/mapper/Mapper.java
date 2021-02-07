package perococco.jdgen.mapper;

import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import perococco.jdgen.core.Couple;
import perococco.jdgen.core.JDGenConfiguration;
import perococco.jdgen.core.Rectangle;
import perococco.jdgen.core.Room;

import java.util.Random;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Mapper {

    public static @NonNull Map perform(
            @NonNull JDGenConfiguration configuration,
            @NonNull ImmutableList<Rectangle> rectangles,
            @NonNull ImmutableList<Room> rooms,
            @NonNull ImmutableList<Couple<Room>> corridors) {
        return new Mapper(configuration, rectangles, rooms, corridors).perform();
    }

    @NonNull JDGenConfiguration configuration;
    @NonNull ImmutableList<Rectangle> rectangles;
    @NonNull ImmutableList<Room> rooms;
    @NonNull ImmutableList<Couple<Room>> corridors;

    private Random random;
    private MapGeometry geometry;
    private MapperConfiguration mapperConfiguration;

    private Map map;

    private @NonNull Map perform() {
        this.intializeRandomGenerator();
        this.computeMapGeometry();
        this.buildMapperConfiguration();
        this.createEmptyMap();
        this.fillCellsForMainRooms();
        this.createCorridors();
        return map;
    }

    private void intializeRandomGenerator() {
        this.random = new Random();
        this.random.setSeed(configuration.seed());
    }

    private void computeMapGeometry() {
        this.geometry = GeometryComputer.compute(rooms);
    }

    private void buildMapperConfiguration() {
        this.mapperConfiguration = new MapperConfiguration(configuration,random,geometry);
    }

    private void createEmptyMap() {
        this.map = Map.create(geometry.size());
    }

    private void fillCellsForMainRooms() {
        MainRoomFiller.fillMainRooms(map,geometry,rooms);
    }

    private void createCorridors() {
        CorridorsBuilder.build(mapperConfiguration, map, corridors);
    }


}
