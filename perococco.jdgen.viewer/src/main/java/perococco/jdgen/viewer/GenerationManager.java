package perococco.jdgen.viewer;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class GenerationManager {

    private GenerationModel generationModel = new GenerationModel();

    private final FXUpdater<ViewerState> fxUpdater = new FXUpdater<>(state -> generationModel.setState(state));

    private Thread thread = null;

    public void generate(double dungeonSize, double roomSize1, double roomSize2) {
        final var task = new Runnable() {
            @Override
            public void run() {
                try {
                    new FXGenerator(fxUpdater).generate((int) dungeonSize, (int) Math.min(roomSize1, roomSize2), (int) Math.max(roomSize1, roomSize2));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
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
