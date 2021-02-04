package perococco.jdgen.graph;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.Couple;
import perococco.jdgen.core.ImmutableVector2D;
import perococco.jdgen.core.Point2D;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Delaunay<O> {

    public static <O> ImmutableList<Couple<O>> triangulize(
            @NonNull ImmutableCollection<O> objects, @NonNull Function<? super O, ? extends Point2D> positionGetter) {
        return new Delaunay<O>(objects, positionGetter).triangulize();
    }


    private final @NonNull ImmutableCollection<O> objects;
    private final @NonNull Function<? super O, ? extends Point2D> positionGetter;

    private final @NonNull Set<Triangle> triangulation = new HashSet<>();

    private Edge[] polygon = null;

    private Triangle superTriangle;

    private ImmutableList<Couple<O>> graph;

    private ImmutableList<Couple<O>> triangulize() {
        this.createSuperTriangle();
        this.addSuperTriangleToTriangulation();
        for (O object : objects) {
            final var point = positionGetter.apply(object);
            formPolygon(point);
            triangulizePolygon(point);
        }
        this.cleanUpTriangulation();
        this.buildMap();

        return graph;
    }

    private void buildMap() {
        final Map<Point2D, O> objectByPosition = objects.stream().collect(Collectors.toMap(positionGetter, o -> o));
        final Function<Couple<Point2D>, Couple<O>> coupleMapper = couple -> couple.map(objectByPosition::get);

        graph = triangulation.stream()
                             .flatMap(Triangle::edgeStream)
                             .distinct()
                             .map(Edge::vertices)
                             .map(coupleMapper)
                             .collect(ImmutableList.toImmutableList());

    }

    private void cleanUpTriangulation() {
        final ImmutableSet<Point2D> pointInSuperTri = superTriangle.vertices();
        triangulation.removeIf(t -> t.edgeStream()
                                     .flatMap(Edge::vertexStream)
                                     .anyMatch(pointInSuperTri::contains));
    }

    private void formPolygon(@NonNull Point2D pointToAdd) {
        final var badTriangles = triangulation.stream()
                                              .filter(t -> t.isPointInsideCircumCircle(pointToAdd))
                                              .collect(Collectors.toSet());
        final var edgeCount = badTriangles.stream()
                                          .flatMap(Triangle::edgeStream)
                                          .collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        this.polygon = edgeCount.entrySet()
                                .stream()
                                .filter(e -> e.getValue() == 1)
                                .map(Map.Entry::getKey)
                                .distinct()
                                .toArray(Edge[]::new);
        triangulation.removeAll(badTriangles);
    }

    private void triangulizePolygon(@NonNull Point2D point) {
        Arrays.stream(polygon).map(e -> e.createTriangle(point)).forEach(triangulation::add);
    }


    private void createSuperTriangle() {
        final var sx = objects.stream().map(positionGetter).collect(Collectors.summarizingDouble(Point2D::x));
        final var sy = objects.stream().map(positionGetter).collect(Collectors.summarizingDouble(Point2D::y));

        final var xmin = sx.getMin();
        final var xmax = sx.getMax();

        final var ymin = sy.getMin();
        final var ymax = sy.getMax();

        final var epsi = (sx.getMax() - sx.getMin()) / 5.0;

        final var a = ImmutableVector2D.of(xmin - epsi, ymin - epsi);
        final var b = ImmutableVector2D.of(xmin + 2 * (xmax - xmin) + 3 * epsi, ymin - epsi);
        final var c = ImmutableVector2D.of(xmin - epsi, ymin + 2 * (ymax - ymin) + 3 * epsi);
        this.superTriangle = new Triangle(a, b, c);
    }

    private void addSuperTriangleToTriangulation() {
        this.triangulation.add(superTriangle);
    }


}
