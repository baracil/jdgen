package perococco.gen.generator._private;

import lombok.NonNull;
import perococco.gen.generator.DungeonGenerator;

import java.util.ServiceLoader;

public class Loader {

    public @NonNull DungeonGenerator create() {
        return create(ServiceLoader.load(DungeonGenerator.class));
    }

    public @NonNull DungeonGenerator create(@NonNull ModuleLayer moduleLayer) {
        return create(ServiceLoader.load(moduleLayer, DungeonGenerator.class));
    }

    private DungeonGenerator create(@NonNull ServiceLoader<DungeonGenerator> serviceLoad) {
        return serviceLoad.stream()
                          .map(ServiceLoader.Provider::get)
                          .findFirst()
                          .orElseGet(PerococcoDungeonGenerator::new);

    }

}
