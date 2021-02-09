package perococco.jdgen.mapper;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.api.Cell;
import perococco.jdgen.core.Couple;
import perococco.jdgen.core.Room;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CorridorsBuilder<C extends Cell>  {

    public static <C extends Cell> void build(@NonNull MapperParameters<C> parameters) {
        new CorridorsBuilder<>(parameters).build();
    }

    private final @NonNull MapperParameters<C> parameters;

    private void build() {
        parameters.forEachCorridors(this::createCorridor);
    }

    private void createCorridor(Couple<Room> couple) {
        OneCorridorBuilder.build(parameters,couple.getValue1(), couple.getValue2());
    }
}
