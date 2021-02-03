package perococco.jdgen.viewer;

import com.google.common.collect.ImmutableList;
import javafx.application.Platform;
import javafx.concurrent.Task;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.JDGenConfiguration;
import perococco.jdgen.core.Rectangle;
import perococco.jdgen.core.Room;
import perococco.jdgen.rooms.CellsGenerator;
import perococco.jdgen.rooms.CellsGenerator2;
import perococco.jdgen.rooms.CellsSeparator;
import perococco.jdgen.rooms.RoomSelector;

import java.util.Optional;

@RequiredArgsConstructor
public class GenerationManager {

    private GenerationModel generationModel = new GenerationModel();

    private final FXUpdater<ImmutableList<Rectangle>> fxUpdater = new FXUpdater<>(l -> generationModel.setCells(l));

    private Thread thread = null;

    public void generate(double dungeonSize, double roomSize1, double roomSize2) {
        var task = new Task<ImmutableList<Room>>() {
            @Override
            protected ImmutableList<Room> call() throws Exception {
                try {
                    Platform.runLater(() -> {
                        generationModel.setCells(ImmutableList.of());
                        generationModel.setRooms(ImmutableList.of());
                    });
                    final var configuration = new JDGenConfiguration((int) dungeonSize, (int) Math.min(roomSize1, roomSize2), (int) Math.max(roomSize1, roomSize2), 1.25);
                    final var cells = CellsGenerator.generate(configuration);
                    fxUpdater.set(cells);

                    final var c = CellsSeparator.separate(configuration,cells,l -> fxUpdater.set(l));
                    return RoomSelector.select(configuration, c);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }

            @Override
            protected void succeeded() {
                generationModel.setRooms(this.getValue());
            }
        };
        this.stop();
        this.thread = new Thread(task);
        this.thread.setDaemon(true);
        this.thread.start();
    }

    public void stop() {
        Optional.ofNullable(thread).ifPresent(Thread::interrupt);
    }


    public ROGenerationModel getGenerationModel() {
        return generationModel;
    }

}
