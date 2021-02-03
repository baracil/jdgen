package perococco.jdgen.rooms;

import com.google.common.collect.ImmutableList;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.distribution.NormalDistribution;
import perococco.jdgen.core.JDGenConfiguration;
import perococco.jdgen.core.Rectangle;
import perococco.jdgen.viewer.Viewer;

@RequiredArgsConstructor
public class RoomViewer {

    private final @NonNull ObjectProperty<ImmutableList<Rectangle>> observableValue = new SimpleObjectProperty<>(ImmutableList.of());

    private final @NonNull JDGenConfiguration configuration;

    public static void main(String[] args) {
        final var configuration = new JDGenConfiguration(20, 2,20,1.5,0.6);

        new RoomViewer(configuration).launch();
    }

    public void launch() {
        final var cells = CellsGenerator.generate(configuration);
        final var t = new Thread(() -> {
            CellsSeparator.separate(configuration, cells, RoomViewer.this::observer);
        });

        Viewer.launch(observableValue,t);

        t.interrupt();

    }

    private void observer(@NonNull ImmutableList<Rectangle> cells) {
        Platform.runLater(() -> observableValue.set(cells));
    }
}
