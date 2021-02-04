package perococco.jdgen.viewer;

import com.google.common.collect.ImmutableList;
import javafx.application.Platform;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.JDGenConfiguration;
import perococco.jdgen.core.Room;
import perococco.jdgen.graph.Delaunay;
import perococco.jdgen.graph.EMSTBuilder;
import perococco.jdgen.rooms.CellsGenerator;
import perococco.jdgen.rooms.CellsSeparator;
import perococco.jdgen.rooms.RoomSelector;

@RequiredArgsConstructor
public class FXGenerator {

    private final FXUpdater<ViewerState> fxUpdater;

    public void generate(int dungeonSize, int minRoomSize, int maxRoomSize) throws Exception {
        fxUpdater.set(ViewerState.initial());

        final var configuration = new JDGenConfiguration(dungeonSize, minRoomSize, maxRoomSize, 1.25);

        final var cells = CellsGenerator.generate(configuration);
        fxUpdater.update(s -> s.withCells(cells));

        final var c = CellsSeparator.separate(configuration, cells, l -> fxUpdater.update(s -> s.withCells(l)));
        final var rooms = RoomSelector.select(configuration, c);
        fxUpdater.update(s -> s.withRooms(rooms));

        final var graph = Delaunay.triangulize(rooms, Room::position);
        fxUpdater.update(s -> s.withDelaunayGraph(graph));

        final var tree = EMSTBuilder.buildTree(graph, Room::position);
        fxUpdater.update(s -> s.withEMSTree(tree));

    }

}
