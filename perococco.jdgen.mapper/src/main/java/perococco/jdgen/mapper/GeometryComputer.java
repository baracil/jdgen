package perococco.jdgen.mapper;

import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.RectangleGeometry;
import perococco.jdgen.core.Room;
import perococco.jdgen.core.Size;

import java.util.IntSummaryStatistics;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GeometryComputer {

    public static @NonNull MapGeometry compute(@NonNull ImmutableList<Room> rooms) {
        return new GeometryComputer(rooms).compute();
    }

    private final @NonNull ImmutableList<Room> rooms;

    private IntSummaryStatistics xStat;
    private IntSummaryStatistics yStat;

    private MapGeometry geometry;

    private @NonNull MapGeometry compute() {
        this.computeXStatistic();
        this.computeYStatistic();
        this.createGeometry();

        return geometry;
    }

    private void computeXStatistic() {
        this.xStat = compute(RectangleGeometry.X_AXIS_GETTER);
    }

    private void computeYStatistic() {
        this.yStat = compute(RectangleGeometry.Y_AXIS_GETTER);
    }

    private void createGeometry() {
        final var size = new Size(xStat.getMax() - xStat.getMin() + 1, yStat.getMax() - yStat.getMin() + 1);

        this.geometry = new MapGeometry(
                -xStat.getMin(),
                -yStat.getMin(),
                size);
    }

    private IntSummaryStatistics compute(@NonNull RectangleGeometry.AxisOperations axisOperations) {
        return rooms.stream()
                    .flatMap(r -> {
                        var p = axisOperations.getCenter(r);
                        var l = axisOperations.getHalfLength(r);
                        return IntStream.of(p - l, p + l).boxed();
                    })
                    .collect(Collectors.summarizingInt(i -> i));
    }

}
