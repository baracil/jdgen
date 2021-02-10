package perococco.jdgen.api._private;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.api.Position;
import perococco.jdgen.api.Transformation;

@RequiredArgsConstructor
public class OffsetTransformation implements Transformation {

    private final int dx;
    private final int dy;

    @Override
    public @NonNull Position apply(@NonNull Position position) {
        return position.translate(dx,dy);
    }

    @Override
    public @NonNull Transformation inverse() {
        return new OffsetTransformation(-dx,-dy);
    }
}
