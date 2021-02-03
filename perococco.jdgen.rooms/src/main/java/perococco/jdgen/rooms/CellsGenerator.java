package perococco.jdgen.rooms;

import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.distribution.NormalDistribution;
import perococco.jdgen.core.JDGenConfiguration;
import perococco.jdgen.core.Rectangle;

import java.util.function.DoubleSupplier;
import java.util.stream.IntStream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CellsGenerator {

    public static ImmutableList<Rectangle> generate(@NonNull JDGenConfiguration configuration) {
        return new CellsGenerator(configuration).generate();
    }

    private final @NonNull JDGenConfiguration configuration;

    private @NonNull ImmutableList<Rectangle> generate() {
        final var nbRooms = (int)Math.floor(configuration.dungeonSize()*(5+Math.random()));
        return IntStream.range(0, nbRooms)
                        .mapToObj(this::createRectangle)
                        .collect(ImmutableList.toImmutableList());
    }

    private @NonNull Rectangle createRectangle(int i) {
        final var randomSupplier = randomSupplier();

        final var width = (int)Math.round(randomSupplier.getAsDouble());
        final var height = (int)Math.round(randomSupplier.getAsDouble());
        return new Rectangle(0,0,width,height);
    }

    private @NonNull DoubleSupplier randomSupplier() {
        final double mean = 0.5 * (configuration.maxRoomSize() + configuration.minRoomSize());
        final double sd = (configuration.maxRoomSize() - configuration.minRoomSize())/6;
        return new NormalDistribution(mean,sd)::sample;
    }

}
