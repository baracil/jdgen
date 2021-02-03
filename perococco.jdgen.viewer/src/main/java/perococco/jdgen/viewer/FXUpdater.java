package perococco.jdgen.viewer;

import javafx.application.Platform;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class FXUpdater<T> {

    private final @NonNull Consumer<? super T> consumerInFx;

    private final AtomicReference<T> reference = new AtomicReference<T>(null);

    public void set(@NonNull T value) {
        if (reference.getAndSet(value) == null) {
            Platform.runLater(this::update);
        }
    }

    private void update() {
        final T value = reference.getAndSet(null);
        consumerInFx.accept(value);
    }
}
