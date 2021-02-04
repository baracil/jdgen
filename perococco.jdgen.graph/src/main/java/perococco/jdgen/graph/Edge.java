package perococco.jdgen.graph;

import com.google.common.collect.ImmutableSet;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
import org.apache.commons.math3.util.Pair;
import perococco.jdgen.core.Couple;
import perococco.jdgen.core.ImmutableVector2D;

import java.util.stream.Stream;

@Value
@EqualsAndHashCode(of="vertices")
public class Edge {

    @NonNull Couple<ImmutableVector2D> vertices;

    public Edge(@NonNull ImmutableVector2D vertex1, @NonNull ImmutableVector2D vertex2) {
        this.vertices = Couple.of(vertex1,vertex2);
    }

    public Couple<ImmutableVector2D> vertices() {
        return vertices;
    }


    public Stream<ImmutableVector2D> vertexStream() {
        return vertices.stream();
    }
}
