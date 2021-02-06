package perococco.jdgen.viewer;

import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.Couple;
import perococco.jdgen.core.Rectangle;
import perococco.jdgen.core.Room;

@Getter
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class ViewerState {

    private final int minRoomSize;

    private final @NonNull ImmutableList<Rectangle> cells;

    private final @NonNull ImmutableList<Room> rooms;

    private final @NonNull ImmutableList<Couple<Room>> delaunayGraph;

    private final @NonNull ImmutableList<Couple<Room>> path;

    public static @NonNull ViewerState initial(int minRoomSize) {
        return new ViewerState(minRoomSize,ImmutableList.of(),ImmutableList.of(), ImmutableList.of(), ImmutableList.of());
    }

    public static @NonNull ViewerState initial() {
        return initial(1);
    }

    public @NonNull ViewerState withMinRoomSize(int minRoomSize) {
        return toBuilder().minRoomSize(minRoomSize).build();
    }

    public @NonNull ViewerState withCells(ImmutableList<Rectangle> cells) {
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
}
