package perococco.jdgen.rooms;

import com.google.common.collect.ImmutableList;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.Cell;
import perococco.jdgen.core.JDGenConfiguration;
import perococco.jdgen.core.RectangleGeometry;
import perococco.jdgen.core.Room;

@RequiredArgsConstructor
public class RoomSelector {

    public static @NonNull ImmutableList<Room> select(@NonNull JDGenConfiguration configuration, @NonNull ImmutableList<Cell> cells) {
        return new RoomSelector(configuration, cells).select();
    }

    private final @NonNull JDGenConfiguration configuration;

    private final @NonNull ImmutableList<Cell> mapCells;

    private ImmutableList<Room> select() {
        final var aw = mapCells.stream().mapToInt(RectangleGeometry::getHalfWidth).average().orElse(0);
        final var ah = mapCells.stream().mapToInt(RectangleGeometry::getHalfHeight).average().orElse(0);
        final var tw = configuration.getMainRoomThreshold() * aw;
        final var th = configuration.getMainRoomThreshold() * ah;

        return mapCells.stream()
                    .filter(c -> (c.getHalfHeight() >= th && c.getHalfWidth() >= tw))
                    .map(Room::new)
                    .collect(ImmutableList.toImmutableList());

    }
}
