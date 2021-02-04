package perococco.jdgen.graph;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.Couple;
import perococco.jdgen.core.ImmutableVector2D;
import perococco.jdgen.core.ROVector2D;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Delaunay {

    public static ImmutableMap<ImmutableVector2D,ImmutableSet<ImmutableVector2D>> triangulize(@NonNull ImmutableSet<ImmutableVector2D> points) {
        return new Delaunay(points).triangulize();
    }


    private final @NonNull ImmutableSet<ImmutableVector2D> input;

    private final @NonNull Set<Triangle> triangulation = new HashSet<>();

    private ImmutableVector2D[] polygon = null;

    private Triangle superTriangle;

    private ImmutableMap<ImmutableVector2D,ImmutableSet<ImmutableVector2D>> graph;

    private ImmutableMap<ImmutableVector2D,ImmutableSet<ImmutableVector2D>> triangulize() {
        this.createSuperTriangle();
        this.addSuperTriangleToTriangulation();
        for (ImmutableVector2D point : input) {
            formPolygon(point);
            triangulizePolygon(point);
        }
        this.cleanUpTriangulation();
        this.buildMap();

        return graph;
    }

    private void buildMap() {
        graph = triangulation.stream()
                     .flatMap(Triangle::edgeStream)
                     .distinct()
                     .flatMap(e -> Stream.of(e.vertices(),e.vertices().swap()))
                     .collect(
                             Collectors.collectingAndThen(
                             Collectors.groupingBy(Couple::value1, Collectors.mapping(Couple::value2, ImmutableSet.toImmutableSet())),
                             ImmutableMap::copyOf)
                     );

    }

    private void cleanUpTriangulation() {
        final Set<Edge> pointInSuperTri = superTriangle.edges();
        triangulation.removeIf(t -> t.edgeStream().anyMatch(pointInSuperTri::contains));
    }

    private void formPolygon(@NonNull ImmutableVector2D pointToAdd) {
            final var badTriangles = triangulation.stream()
                                                  .filter(t -> t.isPointInsideCircumCircle(pointToAdd))
                                                  .collect(Collectors.toSet());
            this.polygon = badTriangles.stream()
                                  .flatMap(Triangle::edgeStream)
                                  .collect(Collectors.groupingBy(e -> e, Collectors.counting()))
                                  .entrySet()
                                  .stream()
                                  .filter(e -> e.getValue() == 1)
                                  .map(Map.Entry::getKey)
                                  .flatMap(Edge::vertexStream)
                                  .distinct()
                                  .toArray(ImmutableVector2D[]::new);
            triangulation.removeAll(badTriangles);
    }

    private void triangulizePolygon(@NonNull ImmutableVector2D point) {
        for (int i = 0; i < polygon.length-1; i++) {
            triangulation.add(new Triangle(polygon[i], polygon[i+1],point.toImmutable()));
        }
        triangulation.add(new Triangle(polygon[polygon.length-1], polygon[0],point.toImmutable()));
    }


    private void createSuperTriangle() {
        final var sx = input.stream().collect(Collectors.summarizingDouble(ImmutableVector2D::x));
        final var sy = input.stream().collect(Collectors.summarizingDouble(ImmutableVector2D::y));

        final var xmin = sx.getMin();
        final var xmax = sx.getMax();

        final var ymin = sy.getMin();
        final var ymax = sy.getMax();

        final var epsi = (sx.getMax()- sx.getMin())/5.0;

        final var a = ImmutableVector2D.of(xmin-epsi, ymin-epsi);
        final var b = ImmutableVector2D.of(xmin+2*(xmax-xmin)+3*epsi, ymin-epsi);
        final var c= ImmutableVector2D.of(xmin-epsi, ymin+2*(ymax-ymin)+3*epsi);
        this.superTriangle = new Triangle(a,b,c);
    }

    private void addSuperTriangleToTriangulation() {
        this.triangulation.add(superTriangle);
    }


}
