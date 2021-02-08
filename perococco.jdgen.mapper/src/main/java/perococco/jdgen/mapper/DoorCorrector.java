package perococco.jdgen.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.IntPoint;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.stream.Stream;

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

    private void applyToAllPositions(@NonNull Consumer<? super IntPoint> action) {
        parameters.getMap()
                  .allMapPositions()
                  .sorted(Comparator.comparingInt(IntPoint::getX).thenComparingInt(IntPoint::getY))
                  .forEach(action);
    }

    private @NonNull CellType getCellTypeAt(IntPoint position) {
        return parameters.getMap().getCellAt(position).getType();
    }

    private void setCellTypeAt(@NonNull CellType cellType, IntPoint position) {
        parameters.getMap().setCellAt(new MapCell(cellType), position);
    }

    private void correctDoor(@NonNull IntPoint position) {
        final var cellType = getCellTypeAt(position);
        if (cellType == CellType.DOOR) {
            checkIfDoorIsValid(position);
        }
    }

    private void correctCorridor(@NonNull IntPoint position) {
        final var cellType = getCellTypeAt(position);
        if (cellType == CellType.CORRIDOR_FLOOR) {
            checkIfCorridorShouldBeADoor(position);
        }
    }

    private void checkIfDoorIsValid(IntPoint position) {
        final var doorIsInvalid = Arrays.stream(Pattern.values())
                                      .noneMatch(p -> p.isDoorValid(parameters.getMap(), position));
        if (doorIsInvalid) {
            setCellTypeAt(CellType.CORRIDOR_FLOOR,position);
        }
    }


    private void checkIfCorridorShouldBeADoor(IntPoint position) {
        final var shouldBeDoor = Arrays.stream(Pattern.values())
                                        .anyMatch(p -> p.shouldBeDoor(parameters.getMap(), position));
        if (shouldBeDoor) {
            setCellTypeAt(CellType.DOOR,position);
        }
    }
}
