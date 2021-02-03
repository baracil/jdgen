package perococco.jdgen.viewer;

import com.google.common.collect.ImmutableList;
import javafx.concurrent.Task;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.JDGenConfiguration;
import perococco.jdgen.core.Rectangle;

import java.util.function.Function;

@RequiredArgsConstructor
public class GenerationManager {

    private GenerationModel generationModel = new GenerationModel();

    private @NonNull Function<JDGenConfiguration,ImmutableList<Rectangle>> generator;

    public void generate(double dungeonSize, double roomSize1, double roomSize2) {
        var task = new Task<ImmutableList<Rectangle>>() {
            @Override
            protected ImmutableList<Rectangle> call() throws Exception {
                final var configuration = new JDGenConfiguration((int)dungeonSize,(int)Math.min(roomSize1,roomSize2), (int)Math.max(roomSize1,roomSize2),1.5);
                return generator.apply(configuration);
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
