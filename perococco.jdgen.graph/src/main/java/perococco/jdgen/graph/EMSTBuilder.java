package perococco.jdgen.graph;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.Couple;
import perococco.jdgen.core.Point2D;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static perococco.jdgen.graph.Edge.EDGE_LENGTH_COMPARATOR;

@RequiredArgsConstructor
public class EMSTBuilder<O> {

    public static <O> ImmutableList<Couple<O>> buildTree(
            @NonNull ImmutableCollection<Couple<O>> objects, @NonNull Function<? super O, ? extends Point2D> positionGetter) {
        return new EMSTBuilder<O>(objects, positionGetter).build();
    }

    private final static Collector<Couple<Point2D>, ?, Map<Point2D, Set<Edge>>> CONNECTIONS_COLLECTOR
            = Collectors.groupingBy(Couple::value1,
                                    Collectors.mapping(Edge::fromCoupleOfPoints, Collectors.toSet()));


    private final @NonNull ImmutableCollection<Couple<O>> edges;

    private final @NonNull Function<? super O, ? extends Point2D> positionGetter;

    private Map<Point2D, Set<Edge>> connections;

    private Set<Point2D> availableVertices;

    private final Set<Point2D> picked = new HashSet<>();

    private final Set<Edge> pickedEdges = new HashSet<>();

    private ImmutableList<Couple<O>> build() {
        this.buildConnections();
        this.prepareVertexSet();
        while (!availableVertices.isEmpty()) {
            if (picked.isEmpty()) {
                var point = this.pickOneEdgeFromTheSet();
                picked.add(point);
                availableVertices.remove(point);
            } else {
                final var smallestEdge = this.findSmalledConnectedEdge();
                smallestEdge.ifPresent(e -> {
                    pickedEdges.add(e);
                    availableVertices.remove(e.value1());
                    availableVertices.remove(e.value2());
                    picked.add(e.value1());
                    picked.add(e.value2());
                });
            }
        }
        return buildResult();
    }

    private void buildConnections() {
        connections = edges.stream()
                .<Couple<Point2D>>map(c -> c.map(positionGetter))
                .flatMap(c -> Stream.of(c, c.swap()))
                .collect(CONNECTIONS_COLLECTOR);
    }


    private @NonNull ImmutableList<Couple<O>> buildResult() {
        final Map<Point2D, O> objectMapper = edges.stream()
                                                  .flatMap(Couple::stream)
                                                  .distinct()
                                                  .collect(Collectors.toMap(positionGetter, o -> o));

        final Function<Couple<Point2D>, Couple<O>> mapper = c -> c.map(objectMapper::get);

        return pickedEdges.stream()
                          .map(Edge::vertices)
                          .map(mapper)
                          .collect(ImmutableList.toImmutableList());

    }

    private @NonNull Optional<Edge> findSmalledConnectedEdge() {
        return picked.stream()
                     .map(this::smallestConnectedEdge)
                     .flatMap(Optional::stream)
                     .min(EDGE_LENGTH_COMPARATOR);
    }

    private @NonNull Optional<Edge> smallestConnectedEdge(@NonNull Point2D point2D) {
        return connections.getOrDefault(point2D, Set.of())
                          .stream()
                          .filter(this::notAlreadyPicked)
                          .min(EDGE_LENGTH_COMPARATOR);
    }

    private boolean notAlreadyPicked(@NonNull Edge edge) {
        return edge.vertexStream().anyMatch(e -> !picked.contains(e));
    }

    private void prepareVertexSet() {
        this.availableVertices = new HashSet<>(connections.keySet());
    }

    private @NonNull Point2D pickOneEdgeFromTheSet() {
        return this.availableVertices.stream()
                                     .filter(p -> !picked.contains(p))
                                     .findAny()
                                     .orElseThrow();
    }


}
