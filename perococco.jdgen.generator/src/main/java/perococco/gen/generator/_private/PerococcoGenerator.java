package perococco.gen.generator._private;

import lombok.NonNull;
import perococco.gen.generator.GenerationToken;
import perococco.gen.generator.Generator;
import perococco.gen.generator.JDGenConfiguration;
import perococco.gen.generator.Map;
import perococco.jdgen.core.Room;
import perococco.jdgen.graph.Delaunay;
import perococco.jdgen.graph.EMSTBuilder;
import perococco.jdgen.graph.PathBuilder;
import perococco.jdgen.mapper.Mapper;
import perococco.jdgen.mapper.MapperParameters;
import perococco.jdgen.rooms.CellCompactor;
import perococco.jdgen.rooms.CellsGenerator;
import perococco.jdgen.rooms.RoomSelector;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PerococcoGenerator implements Generator {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor(PerococcoGenerator::createThreadForDungeonGeneration);

    public PerococcoGenerator() {
    }

    @Override
    public @NonNull GenerationToken generate(@NonNull JDGenConfiguration configuration) {
        final var completableFuture = new CompletableFuture<Map>();
        final var future = EXECUTOR_SERVICE.submit(() -> buildMap(configuration,completableFuture));

        return new PerococcoGenerationToken(completableFuture,future);
    }


    private static void buildMap(@NonNull JDGenConfiguration configuration, @NonNull CompletableFuture<Map> completableFuture) {
        try {
            final var mapCells = CellsGenerator.generate(configuration);
            final var compactedCells = CellCompactor.compact(mapCells);
            final var rooms = RoomSelector.select(configuration, compactedCells);
            final var graph = Delaunay.triangulize(rooms, Room::getPosition);
            final var tree = EMSTBuilder.buildTree(graph, Room::getPosition);
            final var corridors = PathBuilder.buildPath(configuration, graph, tree);
            final var map = Mapper.perform(MapperParameters.create(configuration, compactedCells, rooms, corridors));
            completableFuture.complete(map);
        } catch (Throwable e) {
            completableFuture.completeExceptionally(e);
        }
    }

    private static @NonNull Thread createThreadForDungeonGeneration(@NonNull Runnable runnable) {
        final Thread thread = new Thread(runnable,"Dungeon generation");
        thread.setDaemon(true);
        return thread;
    }
}
