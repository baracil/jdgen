package perococco.jdgen.viewer;

import com.google.common.collect.ImmutableBiMap;
import javafx.scene.layout.HBox;
import lombok.NonNull;

public class MainController {
    public ButtonBarController buttonBarController;


    public void setGenerator() {

    }

    public void setGenerationManager(GenerationManager generationManager) {
        buttonBarController.setGenerationManager(generationManager);

    }
}
