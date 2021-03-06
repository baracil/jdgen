package perococco.jdgen.core;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A couple of value of same type.
 * Two couple are identicals if they contain the same value without taking
 * order into accound (like a set)
 * @param <T>
 */
@RequiredArgsConstructor
@Getter
public class Couple<T> {

    private final @NonNull T value1;
    private final @NonNull T value2;

    public static <T> @NonNull Couple<T> of(@NonNull T vertex1, @NonNull T vertex2) {
        return new Couple<>(vertex1,vertex2);
    }

    public <R> @NonNull Couple<R> map(@NonNull Function<? super T, ? extends R> mapper) {
        final var newValue1 = mapper.apply(value1);
        final var newValue2 = mapper.apply(value2);
        return Couple.of(newValue1,newValue2);
    }

    public @NonNull Couple<T> swap() {
        return new Couple<>(value2,value1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Couple<?> couple = (Couple<?>) o;
        if (Objects.equals(value1, couple.value1) && Objects.equals(value2, couple.value2)) {
            return true;
        }
        return Objects.equals(value2, couple.value1) && Objects.equals(value1, couple.value2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value1, value2) + Objects.hash(value2,value1);
    }

    public Stream<T> stream() {
        return Stream.of(value1,value2);
    }
}
