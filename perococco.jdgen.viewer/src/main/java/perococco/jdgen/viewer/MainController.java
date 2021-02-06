package perococco.jdgen.viewer;

import javafx.fxml.FXML;

public class MainController {
    @FXML
    public ButtonBarController buttonBarController;


    public void setGenerationManager(GenerationManager generationManager) {
        buttonBarController.setGenerationManager(generationManager);

    }
}
