package perococco.jdgen.mapper;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.api.Cell;
import perococco.jdgen.api.CellType;
import perococco.jdgen.api.Map;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Mapper<C extends Cell> {

    public static <C extends Cell> @NonNull Map<C> perform(@NonNull MapperParameters<C> parameters) {
        return new Mapper<>(parameters).perform();
    }

    @NonNull MapperParameters<C> mapperParameters;

    private @NonNull Map<C> perform() {
        this.fillCellsForMainRooms();
        this.createCorridors();
        this.addCellOverCorridors();
        this.correctDoors();
        this.fillWalls();
        return mapperParameters.getMap().clearTransformation();
    }

    private void fillCellsForMainRooms() {
        RoomFiller.fillMainRooms(mapperParameters, CellType.ROOM_FLOOR);
    }

    private void createCorridors() {
        CorridorsBuilder.build(mapperParameters);
    }

    private void addCellOverCorridors() {
        CellAdder.addCells(mapperParameters);
    }

    private void correctDoors() {
        DoorCorrector.correct(mapperParameters);
    }

    private void fillWalls() {
        WallFiller.fill(mapperParameters);
    }




}
