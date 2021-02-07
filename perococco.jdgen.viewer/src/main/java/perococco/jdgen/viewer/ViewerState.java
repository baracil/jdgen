package perococco.jdgen.viewer;

import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.Cell;
import perococco.jdgen.core.Couple;
import perococco.jdgen.core.Room;
import perococco.jdgen.mapper.Map;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class ViewerState {

    private final int minRoomSize;

    private final @NonNull ImmutableList<Cell> cells;

    private final @NonNull ImmutableList<Room> rooms;

    private final @NonNull ImmutableList<Couple<Room>> delaunayGraph;

    private final @NonNull ImmutableList<Couple<Room>> path;

    private final Map map;

    public @NonNull Optional<Map> map() {
        return Optional.ofNullable(map);
    }

    public static @NonNull ViewerState initial(int minRoomSize) {
        return new ViewerState(minRoomSize,ImmutableList.of(),ImmutableList.of(), ImmutableList.of(), ImmutableList.of(),null);
    }

    public static @NonNull ViewerState initial() {
        return initial(1);
    }

    public @NonNull ViewerState withMinRoomSize(int minRoomSize) {
        return toBuilder().minRoomSize(minRoomSize).build();
    }

    public @NonNull ViewerState withCells(ImmutableList<Cell> cells) {
        return toBuilder().cells(cells).build();
    }

    public @NonNull ViewerState withRooms(ImmutableList<Room> rooms) {
        return toBuilder().rooms(rooms).build();
    }

    public @NonNull ViewerState withDelaunayGraph(ImmutableList<Couple<Room>> graph) {
        return toBuilder().delaunayGraph(graph).build();
    }

    public @NonNull ViewerState withPath(@NonNull ImmutableList<Couple<Room>> path) {
        return toBuilder().path(path).build();
    }

    public @NonNull ViewerState withMap(@NonNull Map map) {
        return toBuilder().map(map).build();
    }
}
