package perococco.jdgen.viewer;

import javafx.application.Platform;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

@RequiredArgsConstructor
public class FXUpdater<T> {

    private final @NonNull Consumer<? super T> consumerInFx;

    private final AtomicBoolean updateRequested = new AtomicBoolean(false);

    private T value;

    public void set(@NonNull T value) {
        this.update(v -> value);
    }

    public void update(@NonNull UnaryOperator<T> updater) {
        this.value = updater.apply(this.value);
        if (!updateRequested.getAndSet(true)) {
            Platform.runLater(this::updateInFX);
        }

    }

    private void updateInFX() {
        updateRequested.set(false);
        consumerInFx.accept(this.value);
    }
}
