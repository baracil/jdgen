package perococco.gen.generator._private;

import lombok.NonNull;
import perococco.gen.generator.DungeonGenerator;
import perococco.jdgen.api.Cell;
import perococco.jdgen.api.CellFactory;
import perococco.jdgen.api.JDGenConfiguration;
import perococco.jdgen.api.Map;
import perococco.jdgen.core.Room;
import perococco.jdgen.graph.Delaunay;
import perococco.jdgen.graph.EMSTBuilder;
import perococco.jdgen.graph.PathBuilder;
import perococco.jdgen.mapper.Mapper;
import perococco.jdgen.mapper.MapperParameters;
import perococco.jdgen.rooms.CellCompactor;
import perococco.jdgen.rooms.CellsGenerator;
import perococco.jdgen.rooms.RoomSelector;

public class PerococcoDungeonGenerator implements DungeonGenerator {

    public PerococcoDungeonGenerator() {
    }

    @Override
    public @NonNull <C extends Cell> Map<C> generate(@NonNull JDGenConfiguration configuration, @NonNull CellFactory<C> cellFactory) {
        final var mapCells = CellsGenerator.generate(configuration);
        final var compactedCells = CellCompactor.compact(mapCells);
        final var rooms = RoomSelector.select(configuration, compactedCells);
        final var graph = Delaunay.triangulize(rooms, Room::getPosition);
        final var tree = EMSTBuilder.buildTree(graph, Room::getPosition);
        final var corridors = PathBuilder.buildPath(configuration, graph, tree);
        return Mapper.perform(MapperParameters.create(configuration, cellFactory,compactedCells, rooms, corridors));
    }

}
