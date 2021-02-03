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
import perococco.jdgen.rooms.RoomSelector;

import java.util.function.Function;

@RequiredArgsConstructor
public class GenerationManager {

    private GenerationModel generationModel = new GenerationModel();

    private @NonNull Function<JDGenConfiguration,ImmutableList<Rectangle>> generator;

    public void generate(double dungeonSize, double roomSize1, double roomSize2) {
        generationModel.setCells(ImmutableList.of());
        generationModel.setRooms(ImmutableList.of());
        var task = new Task<ImmutableList<Room>>() {
            @Override
            protected ImmutableList<Room> call() throws Exception {
                try {
                    final var configuration = new JDGenConfiguration((int) dungeonSize, (int) Math.min(roomSize1, roomSize2), (int) Math.max(roomSize1, roomSize2), 1.25);
                    final var cells = CellsGenerator.generate(configuration);
//                    final var cells = generator.apply(configuration);

                    Platform.runLater(() -> generationModel.setCells(cells));

                    return RoomSelector.select(configuration, cells);
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
        new Thread(task).start();
    }

    public ROGenerationModel getGenerationModel() {
        return generationModel;
    }
}
