package perococco.jdgen.rooms;

import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.DoubleSupplier;
import java.util.function.ToIntFunction;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class CellsGenerator {

    public static @NonNull ImmutableList<Cell> generate(@NonNull JDGenConfiguration configuration) {
        return new CellsGenerator(configuration).generate();
    }

    private final Random randomGenerator = new Random();

    private final @NonNull JDGenConfiguration configuration;
    private int nbRooms;

    private DoubleSupplier randomSupplier;

    private List<Rectangle> result;

    private Rectangle inProgress;

    private @NonNull ImmutableList<Cell> generate() {
        randomGenerator.setSeed(configuration.getSeed());
        this.computeTheTotalNumberOfRooms();
        this.createTheRandomGenerator();
        this.prepareOutputList();

        for (int i = 0; i < nbRooms && !Thread.currentThread().isInterrupted(); i++) {
            this.createNewRoom();
            this.moveRoomToAvoidOverlaps();
            this.addRoomToResult();
        }

        return result.stream().map(Cell::new).collect(ImmutableList.toImmutableList());
    }

    private void computeTheTotalNumberOfRooms() {
        this.nbRooms = (int) Math.floor(configuration.getDungeonSize() * (5 + randomGenerator.nextDouble()));
    }

    private void createTheRandomGenerator() {
        this.randomSupplier = MathTool.normalDistribution(randomGenerator,configuration.getMinRoomSize(), configuration.getMaxRoomSize());
    }

    private void prepareOutputList() {
        this.result = new ArrayList<>(nbRooms);
    }

    private void createNewRoom() {
        final var width = pickOneCellLength();
        final var height = pickOneCellLength();

        inProgress = Rectangle.with(0,0, width / 2, height / 2);
    }

    private int pickOneCellLength() {
        return MathTool.makeOdd(Math.max(configuration.getMinRoomSize(), (int) Math.round(randomSupplier.getAsDouble())));
    }

    private void moveRoomToAvoidOverlaps() {
        final var angle = Math.atan2(randomGenerator.nextDouble()*2-1, randomGenerator.nextDouble()*2-1);
        final var dx = Math.cos(angle);
        final var dy = Math.sin(angle);

        final ToIntFunction<Rectangle> dxComputer = (dx >= 0) ? inProgress::displacementToPutRightOf : inProgress::displacementToPutLeftOf;
        final ToIntFunction<Rectangle> dyComputer = (dy >= 0) ? inProgress::displacementToPutBelowOf : inProgress::displacementToPutAboveOf;


        double distance = Double.NEGATIVE_INFINITY;
        var displacement = IntVector.NIL;

        for (Rectangle rectangle : result) {
            final var rdx = dxComputer.applyAsInt(rectangle);
            final var rdy = dyComputer.applyAsInt(rectangle);
            final var lx = rdx / dx;
            final var ly = rdy / dy;
            if (lx <= 0 || ly <= 0) {
                continue;
            }
            final double l;
            final IntVector d;
            if (lx < ly) {
                l = lx;
                d = new IntVector(rdx, (int) (lx * dy));
            } else {
                l = ly;
                d = new IntVector((int) (ly * dx), rdy);
            }
            if (distance < l) {
                distance = l;
                displacement = d;
            }

        }
        inProgress = inProgress.translate(displacement);
    }

    private void addRoomToResult() {
        this.result.add(inProgress);
        this.inProgress = null;
    }

}
