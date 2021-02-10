package perococco.jdgen.api;

import lombok.NonNull;
import perococco.jdgen.api._private.OffsetTransformation;

import java.util.function.UnaryOperator;

public interface Transformation extends UnaryOperator<Position> {

    @NonNull Position apply(@NonNull Position position);

    @NonNull Transformation inverse();

    static @NonNull Transformation offset(int dx, int dy) {
        return new OffsetTransformation(dx,dy);
    }

}
