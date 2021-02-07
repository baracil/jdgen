package perococco.jdgen.mapper;

import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import perococco.jdgen.core.Cell;
import perococco.jdgen.core.Couple;
import perococco.jdgen.core.JDGenConfiguration;
import perococco.jdgen.core.Rectangle;
import perococco.jdgen.core.Room;

import java.util.Random;

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
        MainRoomFiller.fillMainRooms(mapperParameters);
    }

    private void createCorridors() {
        CorridorsBuilder.build(mapperParameters);
    }


}
