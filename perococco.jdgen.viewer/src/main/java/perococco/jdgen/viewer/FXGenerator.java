package perococco.jdgen.viewer;

import lombok.RequiredArgsConstructor;
import perococco.gen.generator.JDGenConfiguration;
import perococco.jdgen.core.Room;
import perococco.jdgen.graph.Delaunay;
import perococco.jdgen.graph.EMSTBuilder;
import perococco.jdgen.graph.PathBuilder;
import perococco.jdgen.mapper.Mapper;
import perococco.jdgen.mapper.MapperParameters;
import perococco.jdgen.rooms.CellCompactor;
import perococco.jdgen.rooms.CellsGenerator;
import perococco.jdgen.rooms.RoomSelector;

@RequiredArgsConstructor
public class FXGenerator {

    private final FXUpdater<ViewerState> fxUpdater;

    public void generate(int dungeonSize, int minRoomSize, int maxRoomSize, long seed)  {
        fxUpdater.set(ViewerState.initial(minRoomSize));

        //dee66a063bc3cf09
        //66f4aba0c5ffa174 //5-5-2
        //9292867ca9ccf846

        final var configuration = new JDGenConfiguration(seed, dungeonSize, minRoomSize, maxRoomSize, 1.25);

        final var mapCells = CellsGenerator.generate(configuration);
        fxUpdater.update(s -> s.withCells(mapCells));

        final var compactedCells = CellCompactor.compact(mapCells, l -> fxUpdater.update(s -> s.withCells(l)));
        final var rooms = RoomSelector.select(configuration, compactedCells);
        fxUpdater.update(s -> s.withRooms(rooms));


        final var graph = Delaunay.triangulize(rooms, Room::getPosition);

        fxUpdater.update(s -> s.withDelaunayGraph(graph));


        final var tree = EMSTBuilder.buildTree(graph, Room::getPosition);
        fxUpdater.update(s -> s.withPath(tree));

        final var corridors = PathBuilder.buildPath(configuration,graph,tree);
        fxUpdater.update(s -> s.withPath(corridors));

        final var map = Mapper.perform(MapperParameters.create(configuration, compactedCells, rooms, corridors));

        fxUpdater.update(s -> s.withMap(map));


    }

}
