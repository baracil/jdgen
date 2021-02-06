package perococco.jdgen.viewer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Viewer extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    private Dungeon dungeon = new Dungeon();

    private final ZoomOperator zoomOperator = new ZoomOperator(true);

    @Override
    public void start(Stage primaryStage) throws Exception {
        final GenerationManager generationManager = new GenerationManager();
        final FXMLLoader loader = new FXMLLoader(Viewer.class.getResource("main.fxml"));
        final BorderPane container = loader.load();
        final MainController controller = loader.getController();

        controller.setGenerationManager(generationManager);

        dungeon.setModel(generationManager.getGenerationModel());

        final var pane = new Pane();
        pane.getChildren().add(dungeon);
        container.setCenter(pane);


        final Scene scene = new Scene(container, 600, 400);

        pane.setTranslateX(300);
        pane.setTranslateY(200);

        scene.setOnScroll(e -> {
            double zoomFactor = 1.5;
            if (e.getDeltaY() == 0) {
                return;
            }
            if (e.getDeltaY() < 0) {
                zoomFactor = 1. / 1.5;
            }
            if (e.getDeltaY() > 0) {
                zoomFactor = 1.5;
            }
            zoomOperator.zoom(pane, zoomFactor, e.getSceneX(), e.getSceneY());
        });


        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

}
