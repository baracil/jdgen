package perococco.jdgen.core;

import lombok.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

public class TestCouple {

    public static Stream<Arguments> samples() {
        return Stream.of(
                Arguments.of("hello","bonjour"),
                Arguments.of(1,5),
                Arguments.of(Set.of("coucou"),Set.of("hello"))
        );
    }

    @ParameterizedTest
    @MethodSource("samples")
    public void shouldBeEqual(@NonNull Object v1, @NonNull Object v2) {
        final var c1 = Couple.of(v1,v2);
        final var c2 = Couple.of(v2,v1);
        Assertions.assertEquals(c1,c2);
    }
}
