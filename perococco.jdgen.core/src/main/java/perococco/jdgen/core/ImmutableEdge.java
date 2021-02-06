package perococco.jdgen.core;

import lombok.NonNull;
import lombok.Value;

@Value
public class ImmutableEdge {
    @NonNull Vector2D vertex1;
    @NonNull Vector2D vertex2;
}
