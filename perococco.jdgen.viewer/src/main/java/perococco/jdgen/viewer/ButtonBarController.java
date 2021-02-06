package perococco.jdgen.viewer;

import javafx.event.ActionEvent;
import javafx.scene.control.Slider;

public class ButtonBarController {

    public Slider dungeonSize;
    public Slider minLength;
    public Slider ratio;
    private GenerationManager generationManager;

    public void setGenerationManager(GenerationManager generationManager) {
        this.generationManager = generationManager;
    }

    public void generate() {
        generationManager.generate(dungeonSize.getValue(), minLength.getValue(), minLength.getValue()*ratio.getValue());
    }
}
