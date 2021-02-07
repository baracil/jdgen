package perococco.jdgen.core;

import lombok.NonNull;
import lombok.Value;

import java.util.Optional;
import java.util.Random;

@Value
public class Overlap {

    /**
     * Start of the overlap
     */
    int start;
    /**
     * end of the overlap inclusive (this coordinate is part of the overlap)
     */
    int end;

    public @NonNull Optional<Overlap> shrinkBy(int margin) {
        final var newStart = start+margin;
        final var newEnd = end-margin;
        if (newStart<=newEnd) {
            return Optional.of(new Overlap(newStart, newEnd));
        }
        return Optional.empty();
    }
    public int size() {
        return end-start+1;
    }

    public int pickAtRandom(@NonNull Random random) {
        return random.nextInt(size())+start;
    }
}
