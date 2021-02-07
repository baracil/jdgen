package perococco.jdgen.graph;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.Couple;
import perococco.jdgen.core.Point2D;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Delaunay<O> {

    /**
     * Perform a Delaunay triangulation for a set of objects by using the
     * <a href="https://en.wikipedia.org/wiki/Bowyer%E2%80%93Watson_algorithm#Pseudocode">Bowyer-Watson algorithm</a>
     *
     * @param objects        the objects to triangulize
     * @param positionGetter a method that return the position of an object
     * @param <O>            the type of the objects
     * @return the list of the edge of the Delaunay triangulation
     */
    public static <O> @NonNull ImmutableList<Couple<O>> triangulize(
            @NonNull ImmutableCollection<O> objects,
            @NonNull Function<? super O, ? extends Point2D> positionGetter) {
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

        objects.stream()
               .map(positionGetter)
               .forEach(point -> {
                   findBadTrianglesAndFormPolygon(point);
                   triangulizePolygon(point);
               });

        this.cleanUpTriangulation();
        this.constructFinalGraph();

        return graph;
    }


    private void createSuperTriangle() {
        final var sx = objects.stream().map(positionGetter).collect(Collectors.summarizingDouble(Point2D::getX));
        final var sy = objects.stream().map(positionGetter).collect(Collectors.summarizingDouble(Point2D::getY));

        final var xmin = sx.getMin();
        final var xmax = sx.getMax();

        final var ymin = sy.getMin();
        final var ymax = sy.getMax();

        final var epsi = (sx.getMax() - sx.getMin()) / 5.0;

        final var a = Point2D.of(xmin - epsi, ymin - epsi);
        final var b = Point2D.of(xmin + 2 * (xmax - xmin) + 3 * epsi, ymin - epsi);
        final var c = Point2D.of(xmin - epsi, ymin + 2 * (ymax - ymin) + 3 * epsi);
        this.superTriangle = new Triangle(a, b, c);
    }

    private void addSuperTriangleToTriangulation() {
        this.triangulation.add(superTriangle);
    }

    private void findBadTrianglesAndFormPolygon(@NonNull Point2D pointToAdd) {
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

    private void cleanUpTriangulation() {
        final ImmutableSet<Point2D> pointInSuperTri = superTriangle.getVertices();
        triangulation.removeIf(t -> t.edgeStream()
                                     .flatMap(Edge::vertexStream)
                                     .anyMatch(pointInSuperTri::contains));
    }

    private void constructFinalGraph() {
        final Map<Point2D, O> objectsByPosition = objects.stream().collect(Collectors.toMap(positionGetter, Function.identity()));
        final Function<Couple<Point2D>, Couple<O>> coupleMapper = couple -> couple.map(objectsByPosition::get);

        graph = triangulation.stream()
                             .flatMap(Triangle::edgeStream)
                             .map(Edge::getVertices)
                             .distinct()
                             .map(coupleMapper)
                             .collect(ImmutableList.toImmutableList());

    }


}
