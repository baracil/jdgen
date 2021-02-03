package perococco.jdgen.viewer;

import javafx.event.ActionEvent;
import javafx.scene.control.Slider;

public class ButtonBarController {

    public Slider dungeonSize;
    public Slider roomSize1;
    public Slider roomSize2;
    private GenerationManager generationManager;

    public void setGenerationManager(GenerationManager generationManager) {
        this.generationManager = generationManager;
    }

    public void generate(ActionEvent actionEvent) {
        generationManager.generate(dungeonSize.getValue(),roomSize1.getValue(), roomSize2.getValue());
    }
}
