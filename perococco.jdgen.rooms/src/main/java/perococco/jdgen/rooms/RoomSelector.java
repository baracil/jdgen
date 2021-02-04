package perococco.jdgen.rooms;

import com.google.common.collect.ImmutableList;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.JDGenConfiguration;
import perococco.jdgen.core.Rectangle;
import perococco.jdgen.core.Room;

@RequiredArgsConstructor
public class RoomSelector {

    public static @NonNull ImmutableList<Room> select(@NonNull JDGenConfiguration configuration, @NonNull ImmutableList<Rectangle> cells) {
        return new RoomSelector(configuration, cells).select();
    }

    private final @NonNull JDGenConfiguration configuration;

    private final @NonNull ImmutableList<Rectangle> cells;

    private ImmutableList<Room> select() {
        final var aw = cells.stream().mapToInt(Rectangle::halfWidth).average().orElse(0);
        final var ah = cells.stream().mapToInt(Rectangle::halfHeight).average().orElse(0);
        final var tw = configuration.mainRoomThreshold() * aw;
        final var th = configuration.mainRoomThreshold() * ah;

        return cells.stream()
                    .filter(c -> (c.halfHeight() >= th && c.halfWidth() >= tw))
                    .map(c -> new Room(c))
                    .collect(ImmutableList.toImmutableList());

    }
}