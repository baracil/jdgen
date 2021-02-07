package perococco.jdgen.core;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
public class Exec<T> {

    private final @NonNull T value;

    public static <T> Exec<T> with(@NonNull T value) {
        return new Exec<>(value);
    }

    public @NonNull <U> Exec<U> map(@NonNull Function<? super T, ? extends U> mapper) {
        return new Exec<>(mapper.apply(value));
    }

    public @NonNull T run(@NonNull Consumer<T> action) {
        action.accept(value);
        return value;
    }
}
