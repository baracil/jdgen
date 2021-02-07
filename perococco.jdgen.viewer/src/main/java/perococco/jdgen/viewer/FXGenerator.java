package perococco.jdgen.viewer;

import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.JDGenConfiguration;
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

    public void generate(int dungeonSize, int minRoomSize, int maxRoomSize, long seed) throws Exception {
        fxUpdater.set(ViewerState.initial(minRoomSize));

        //2032149330135465102
        //-124302025836551960
        //-8321297611080575982
        //7fcae4efcb27e254
        //e62c9015eff868e

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
