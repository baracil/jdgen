package perococco.jdgen.viewer;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
    private MapView mapView = new MapView();

    private final ZoomOperator zoomOperator = new ZoomOperator(true);

    private final BooleanProperty showMapView = new SimpleBooleanProperty(false);

    @Override
    public void start(Stage primaryStage) throws Exception {
        final GenerationManager generationManager = new GenerationManager();
        final FXMLLoader loader = new FXMLLoader(Viewer.class.getResource("main.fxml"));
        final BorderPane container = loader.load();
        final MainController controller = loader.getController();

        controller.setGenerationManager(generationManager);

        dungeon.setModel(generationManager.getGenerationModel());
        mapView.setModel(generationManager.getGenerationModel());
        mapView.setStyle("-fx-background-color: red");

        final var pane = new Pane();
        container.setCenter(pane);
        showMapView.addListener((l,o,showMap) -> {
            if (showMap) {
                System.out.println("SET MAP VIEW TO PANE");
                pane.getChildren().setAll(mapView);
                mapView.widthProperty().bind(pane.widthProperty());
                mapView.heightProperty().bind(pane.heightProperty());
            } else {
                System.out.println("SET DUNGEON VIEW TO PANE");
                pane.getChildren().setAll(dungeon);
            }
        });

        showMapView.bind(Bindings.createBooleanBinding(
                () -> generationManager.getGenerationModel()
                                       .getState()
                                       .map().isPresent(),
                generationManager.getGenerationModel().stateProperty()));

        pane.getChildren().setAll(dungeon);


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
