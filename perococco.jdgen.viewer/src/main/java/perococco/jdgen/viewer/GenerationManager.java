package perococco.jdgen.viewer;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class GenerationManager {

    private final GenerationModel generationModel = new GenerationModel();

    private final FXUpdater<ViewerState> fxUpdater = new FXUpdater<>(generationModel::setState);

    private Thread thread = null;

    public void generate(double dungeonSize, double roomSize1, double roomSize2, long seed) {
        final Runnable task = () -> {
            try {
                new FXGenerator(fxUpdater).generate((int) dungeonSize, (int) Math.min(roomSize1, roomSize2), (int) Math.max(roomSize1, roomSize2),seed);
            } catch (Exception error) {
                System.err.println("Generation failed");
                error.printStackTrace();
                fxUpdater.set(ViewerState.initial((int)roomSize1));
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


    public @NonNull ROGenerationModel getGenerationModel() {
        return generationModel;
    }

}
