package perococco.jdgen.core;

import lombok.NonNull;
import lombok.Value;

@Value
public class ImmutableEdge {
    @NonNull ImmutableVector2D vertex1;
    @NonNull ImmutableVector2D vertex2;
}
