package perococco.jdgen.mapper;

import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.Couple;
import perococco.jdgen.core.Room;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CorridorsBuilder {

    public static void build(@NonNull MapperParameters parameters) {
        new CorridorsBuilder(parameters).build();
    }

    private final @NonNull MapperParameters parameters;

    private void build() {
        parameters.forEachCorridors(this::createCorridor);
    }

    private void createCorridor(Couple<Room> couple) {
        OneCorridorBuilder.build(parameters,couple.getValue1(), couple.getValue2());
    }
}
