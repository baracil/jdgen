package perococco.jdgen.graph;

import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.Couple;
import perococco.jdgen.core.JDGenConfiguration;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class PathBuilder<O> {

    public static <O> @NonNull ImmutableList<Couple<O>> buildPath(
            @NonNull JDGenConfiguration configuration,
            @NonNull ImmutableList<Couple<O>> graph,
            @NonNull ImmutableList<Couple<O>> msTree) {
        return new PathBuilder<O>(configuration,graph, msTree).buildPath();
    }

    private final @NonNull JDGenConfiguration configuration;
    private final @NonNull ImmutableList<Couple<O>> graph;
    private final @NonNull ImmutableList<Couple<O>> msTree;

    private List<Couple<O>> notUsed;

    private List<Couple<O>> samples;

    public @NonNull ImmutableList<Couple<O>> buildPath() {
        this.extractNotUsedInTree();
        this.shuffleNotUsedEdges();
        this.pickSomeNotUsedEdges();
        return Stream.concat(
                msTree.stream(),
                samples.stream()
        ).collect(ImmutableList.toImmutableList());
    }

    private void extractNotUsedInTree() {
        final Set<Couple<O>> inTree = new HashSet<>(msTree);
        this.notUsed = graph.stream().filter(o -> !inTree.contains(o)).collect(Collectors.toList());
    }

    private void shuffleNotUsedEdges() {
        final Random random = new Random();
        random.setSeed(configuration.getSeed());
        Collections.shuffle(notUsed, random);
    }


    private void pickSomeNotUsedEdges() {
        final int nbToPeek = Math.max(1, Math.round(graph.size() * 0.15f));
        this.samples = this.notUsed.stream()
                                   .limit(nbToPeek)
                                   .collect(Collectors.toList());
    }


}
