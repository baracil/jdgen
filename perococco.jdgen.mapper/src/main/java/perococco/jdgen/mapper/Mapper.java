package perococco.jdgen.mapper;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.gen.generator.CellType;
import perococco.gen.generator.Map;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Mapper {

    public static @NonNull Map perform(@NonNull MapperParameters parameters) {
        return new Mapper(parameters).perform();
    }

    @NonNull MapperParameters mapperParameters;

    private @NonNull Map perform() {
        this.fillCellsForMainRooms();
        this.createCorridors();
        this.addCellOverCorridors();
        this.correctDoors();
        this.fillWalls();
        return mapperParameters.getMap();
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
