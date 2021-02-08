package perococco.gen.generator;

import lombok.NonNull;
import perococco.gen.generator._private.PerococcoDungeonGenerator;
import perococco.jdgen.api.JDGenConfiguration;
import perococco.jdgen.api.Map;

import java.util.ServiceLoader;

public interface DungeonGenerator {

    @NonNull Map generate(@NonNull JDGenConfiguration configuration);

    static @NonNull DungeonGenerator create() {
        return ServiceLoader.load(DungeonGenerator.class)
                            .stream()
                            .map(ServiceLoader.Provider::get)
                            .findFirst()
                            .orElseGet(PerococcoDungeonGenerator::new);
    }

}
