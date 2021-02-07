package perococco.jdgen.mapper;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Mapper {

    public static @NonNull Map perform(@NonNull MapperParameters parameters) {
        return new Mapper(parameters).perform();
    }

    @NonNull MapperParameters mapperParameters;

    private @NonNull Map perform() {
        this.fillCellsForMainRooms();
        this.createCorridors();
        return mapperParameters.getMap();
    }

    private void fillCellsForMainRooms() {
        RoomFiller.fillMainRooms(mapperParameters, CellType.ROOM_FLOOR);
    }

    private void createCorridors() {
        CorridorsBuilder.build(mapperParameters);
    }


}
