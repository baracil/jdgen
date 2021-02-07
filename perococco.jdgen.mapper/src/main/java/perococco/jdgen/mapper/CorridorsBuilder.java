package perococco.jdgen.mapper;

import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.Couple;
import perococco.jdgen.core.Room;

import java.util.Random;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CorridorsBuilder {

    public static void build(@NonNull MapperConfiguration configuration, @NonNull Map map, @NonNull ImmutableList<Couple<Room>> rooms) {
        new CorridorsBuilder(configuration, map, rooms).build();
    }

    private final @NonNull MapperConfiguration configuration;
    private final @NonNull Map map;
    private final @NonNull ImmutableList<Couple<Room>> corridors;

    private void build() {
        corridors.forEach(this::createCorridor);
    }

    private void createCorridor(Couple<Room> couple) {
        OneCorridorBuilder.build(configuration, map,couple.getValue1().getRectangle(), couple.getValue2().getRectangle());
    }
}
