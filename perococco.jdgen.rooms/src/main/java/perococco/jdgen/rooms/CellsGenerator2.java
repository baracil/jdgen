package perococco.jdgen.rooms;

import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.JDGenConfiguration;
import perococco.jdgen.core.MathTool;
import perococco.jdgen.core.Rectangle;

import java.util.List;
import java.util.function.DoubleSupplier;
import java.util.stream.IntStream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CellsGenerator2 {

    public static ImmutableList<Rectangle> generate(@NonNull JDGenConfiguration configuration) {
        return new CellsGenerator2(configuration).generate();
    }

    private final @NonNull JDGenConfiguration configuration;
    private int nbRooms;

    private DoubleSupplier randomSupplier;

    private List<Rectangle> result;

    private Rectangle inProgress;

    private @NonNull ImmutableList<Rectangle> generate() {
        this.computeTheTotalNumberOfRooms();
        this.createTheRandomGenerator();

        return IntStream.range(0, nbRooms)
                        .mapToObj(i -> createNewRoom())
                        .collect(ImmutableList.toImmutableList());

    }

    private void computeTheTotalNumberOfRooms() {
        this.nbRooms = (int) Math.floor(configuration.dungeonSize() * (5 + Math.random()));
    }

    private void createTheRandomGenerator() {
        this.randomSupplier = MathTool.normalDistribution(configuration.minRoomSize(), configuration.maxRoomSize());
    }

    private @NonNull Rectangle createNewRoom() {
        final var width = MathTool.makeOdd(Math.max(configuration.minRoomSize(), (int) Math.round(randomSupplier.getAsDouble())));
        final var height = MathTool.makeOdd(Math.max(configuration.minRoomSize(), (int) Math.round(randomSupplier.getAsDouble())));

        final var angle = Math.random()*Math.PI*2;
        final var radius = Math.sqrt(Math.random());

        final double dx = radius*Math.cos(angle);
        final double dy = radius*Math.sin(angle);

        return new Rectangle((int)Math.round(dx), (int)Math.round(dy), width / 2, height / 2);
    }


}
