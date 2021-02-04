package perococco.jdgen.graph;

import com.google.common.collect.ImmutableSet;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import perococco.jdgen.core.Point2D;
import perococco.jdgen.core.ROVector2D;

import java.util.Set;
import java.util.stream.Stream;

@Value
@EqualsAndHashCode(of={"vertices"})
@ToString(of = "vertices")
public class Triangle {
    @NonNull ImmutableSet<Point2D> vertices;

    @NonNull ImmutableSet<Edge> edges;

    @NonNull Circle circumCircle;

    @NonNull Point2D vertex1;
    @NonNull Point2D vertex2;
    @NonNull Point2D vertex3;

    public Triangle(@NonNull Point2D vertex1,
                    @NonNull Point2D vertex2,
                    @NonNull Point2D vertex3) {
        this.vertices = ImmutableSet.of(vertex1,vertex2,vertex3);
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.vertex3 = vertex3;
        this.edges = ImmutableSet.of(new Edge(vertex1,vertex2), new Edge(vertex2,vertex3),new Edge(vertex3,vertex1));
        this.circumCircle = Circle.circumscribedCircleOf(vertex1, vertex2, vertex3);
    }

    public @NonNull Stream<Edge> edgeStream() {
        return edges.stream();
    }

    public boolean isInside(@NonNull ROVector2D point) {
        return Tools.isInTriangle(point,this);
    }

    public boolean isPointInsideCircumCircle(Point2D point) {
        return circumCircle.isInside(point);
    }

    public ImmutableSet<Point2D> points() {
        return vertices;
    }

    public Set<Edge> edges() {
        return edges;
    }
}
