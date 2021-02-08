package perococco.gen.generator;

import lombok.NonNull;
import perococco.gen.generator._private.PerococcoGenerator;

import java.util.ServiceLoader;

public interface Generator {

    @NonNull GenerationToken generate(@NonNull JDGenConfiguration configuration);

    static @NonNull Generator create() {
        return ServiceLoader.load(Generator.class)
                            .stream()
                            .map(ServiceLoader.Provider::get)
                            .findFirst()
                            .orElseGet(PerococcoGenerator::new);
    }

}
