package perococco.jdgen.mapper;


import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.gen.generator.CellType;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RoomFiller {

    public static void fillMainRooms(@NonNull MapperParameters parameters, @NonNull CellType cellType) {
        new RoomFiller(parameters, cellType).fill();
    }

    private final @NonNull MapperParameters parameters;

    private final @NonNull CellType cellType;

    private void fill() {
        parameters.forEachRooms(OneRoomFiller.createFiller(parameters,cellType,false));
    }

}
