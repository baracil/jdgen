package perococco.jdgen.graph;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import perococco.jdgen.core.Couple;
import perococco.jdgen.core.Point2D;

import java.util.Comparator;
import java.util.stream.Stream;

@Value
@EqualsAndHashCode(of = "vertices")
public class Edge {

    public static final Comparator<Edge> EDGE_LENGTH_COMPARATOR = Comparator.comparingDouble(Edge::getLength);

    @Getter
    @NonNull Couple<Point2D> vertices;

    @Getter
    double length;

    public Edge(@NonNull Point2D vertex1,@NonNull Point2D vertex2) {
        this.vertices = Couple.of(vertex1,vertex2);
        length = Tools.distance(vertex1,vertex2);
    }

    public Stream<Point2D> vertexStream() {
        return vertices.stream();
    }

    public @NonNull Triangle createTriangle(@NonNull Point2D point2D) {
        return new Triangle(vertices.getValue1(),vertices.getValue2(),point2D);
    }

    public static @NonNull Edge fromCoupleOfPoints(@NonNull Couple<Point2D> couple) {
        return new Edge(couple.getValue1(),couple.getValue2());
    }

    @NonNull
    public Point2D getValue1() {
        return vertices.getValue1();
    }

    @NonNull
    public Point2D getValue2() {
        return vertices.getValue2();
    }

}
