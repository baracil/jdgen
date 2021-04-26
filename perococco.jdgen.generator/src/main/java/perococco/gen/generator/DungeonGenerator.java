package perococco.gen.generator;

import lombok.NonNull;
import perococco.gen.generator._private.Loader;
import perococco.gen.generator._private.PerococcoDungeonGenerator;
import perococco.gen.generator._private.SimpleCell;
import perococco.jdgen.api.Cell;
import perococco.jdgen.api.CellFactory;
import perococco.jdgen.api.JDGenConfiguration;
import perococco.jdgen.api.Map;

import java.util.ServiceLoader;

public interface DungeonGenerator {

    <C extends Cell> @NonNull Map<C> generate(@NonNull JDGenConfiguration configuration, @NonNull CellFactory<C> cellFactory);

    default @NonNull Map<Cell> generate(@NonNull JDGenConfiguration configuration) {
        return generate(configuration, new SimpleCell.Factory());
    }

    static @NonNull DungeonGenerator create() {
        return new Loader().create();
    }

    static @NonNull DungeonGenerator create(@NonNull ModuleLayer moduleLayer) {
        return new Loader().create(moduleLayer);
    }

}
