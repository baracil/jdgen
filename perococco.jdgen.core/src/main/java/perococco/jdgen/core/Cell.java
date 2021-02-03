package perococco.jdgen.core;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Cell {

    private final @NonNull Rectangle rectangle;

    private final double dx;

    private final double dy;
}
