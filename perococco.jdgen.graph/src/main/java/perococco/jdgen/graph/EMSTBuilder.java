package perococco.jdgen.graph;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.Couple;
import perococco.jdgen.core.Point2D;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static perococco.jdgen.graph.Edge.EDGE_LENGTH_COMPARATOR;

@RequiredArgsConstructor
public class EMSTBuilder<O> {

    public static <O> ImmutableList<Couple<O>> buildTree(
            @NonNull ImmutableCollection<Couple<O>> objects,
            @NonNull Function<? super O, ? extends Point2D> positionGetter) {
        return new EMSTBuilder<O>(objects, positionGetter).build();
    }

    private final static Collector<Couple<Point2D>, ?, Map<Point2D, Set<Edge>>> CONNECTIONS_COLLECTOR
            = Collectors.groupingBy(Couple::getValue1,
                                    Collectors.mapping(Edge::fromCoupleOfPoints, Collectors.toSet()));


    private final @NonNull ImmutableCollection<Couple<O>> edges;

    private final @NonNull Function<? super O, ? extends Point2D> positionGetter;

    /**
     * For a vertex, gives all the edges with this vertex
     */
    private Map<Point2D, Set<Edge>> connections;

    /**
     * The vertices not yet added to the tree
     */
    private Set<Point2D> availableVertices;

    /**
     * the vertices that are in the tree
     */
    private final Set<Point2D> pickedVertices = new HashSet<>();

    /**
     * The edges that form the EMST
     */
    private final Set<Edge> pickedEdges = new HashSet<>();

    private ImmutableList<Couple<O>> build() {
        this.buildConnections();
        this.prepareAvailableVertices();
        this.pickFirstPointAtRandom();
        while (!availableVertices.isEmpty()) {
            this.findSmalledConnectedEdge()
                .ifPresent(edge -> {
                    pickedEdges.add(edge);
                    edge.vertexStream().forEach(this::setPointPicked);
                });
        }
        return buildResult();
    }

    private void buildConnections() {
        connections = edges.stream()
                .<Couple<Point2D>>map(c -> c.map(positionGetter))
                .flatMap(c -> Stream.of(c, c.swap()))
                .collect(CONNECTIONS_COLLECTOR);
    }

    private void prepareAvailableVertices() {
        this.availableVertices = new HashSet<>(connections.keySet());
    }

    private void pickFirstPointAtRandom() {
        final var picked = this.availableVertices.stream()
                                                 .findAny()
                                                 .orElseThrow();
        this.setPointPicked(picked);
    }

    private void setPointPicked(@NonNull Point2D point2D) {
        this.availableVertices.remove(point2D);
        this.pickedVertices.add(point2D);
    }

    private @NonNull Optional<Edge> findSmalledConnectedEdge() {
        return pickedVertices.stream()
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
        return edge.vertexStream().anyMatch(e -> !pickedVertices.contains(e));
    }


    private @NonNull ImmutableList<Couple<O>> buildResult() {
        final Map<Point2D, O> objectMapper = edges.stream()
                                                  .flatMap(Couple::stream)
                                                  .distinct()
                                                  .collect(Collectors.toMap(positionGetter, o -> o));

        final Function<Couple<Point2D>, Couple<O>> mapper = c -> c.map(objectMapper::get);

        return pickedEdges.stream()
                          .map(Edge::getVertices)
                          .map(mapper)
                          .collect(ImmutableList.toImmutableList());

    }


}
