package perococco.jdgen.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.api.CellType;
import perococco.jdgen.api.Position;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class DoorCorrector {

    public static void correct(@NonNull MapperParameters parameters) {
        new DoorCorrector(parameters).correct();//53a444e93732424f
    }

    private final @NonNull MapperParameters parameters;

    private void correct() {
        applyToAllPositions(this::correctCorridor);
        applyToAllPositions(this::correctDoor);
    }

    private void applyToAllPositions(@NonNull Consumer<? super Position> action) {
        parameters.getMap()
                  .allMapPositions()
                  .sorted(Comparator.comparingInt(Position::getX).thenComparingInt(Position::getY))
                  .forEach(action);
    }

    private @NonNull CellType getCellTypeAt(Position position) {
        return parameters.getMap().getCellAt(position).getType();
    }

    private void correctDoor(@NonNull Position position) {
        final var cellType = getCellTypeAt(position);
        if (cellType == CellType.DOOR) {
            checkIfDoorIsValid(position);
        }
    }

    private void correctCorridor(@NonNull Position position) {
        final var cellType = getCellTypeAt(position);
        if (cellType == CellType.CORRIDOR_FLOOR) {
            checkIfCorridorShouldBeADoor(position);
        }
    }

    private void checkIfDoorIsValid(Position position) {
        final var doorIsInvalid = Arrays.stream(Pattern.values())
                                      .noneMatch(p -> p.isDoorValid(parameters.getMap(), position));
        if (doorIsInvalid) {
            parameters.setCellTypeAt(CellType.CORRIDOR_FLOOR,position);
        }
    }


    private void checkIfCorridorShouldBeADoor(Position position) {
        final var shouldBeDoor = Arrays.stream(Pattern.values())
                                        .anyMatch(p -> p.shouldBeDoor(parameters.getMap(), position));
        if (shouldBeDoor) {
            parameters.setCellTypeAt(CellType.DOOR,position);
        }
    }
}
