package perococco.jdgen.viewer;

import com.google.common.collect.Table;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Optional;

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

        final var graphPane = new Pane(dungeon);
        final var mapPane = new Pane(mapView);
        final var tabPane = new TabPane();

        mapView.heightProperty().bind(mapPane.heightProperty());
        mapView.widthProperty().bind(mapPane.widthProperty());

        tabPane.getTabs().add(new Tab("Graph",graphPane));
        tabPane.getTabs().add(new Tab("Map",mapPane));

        container.setCenter(tabPane);

        final Scene scene = new Scene(container, 600, 400);

        graphPane.setTranslateX(300);
        graphPane.setTranslateY(200);

        scene.setOnScroll(e -> {
            final double zoomFactor;
            if (e.getDeltaY() < 0) {
                zoomFactor = 1. / 1.5;
            }
            else if (e.getDeltaY() > 0) {
                zoomFactor = 1.5;
            } else {
                return;
            }
            Optional.ofNullable(tabPane.getSelectionModel().getSelectedItem())
                    .map(t -> t.getContent())
                    .ifPresent(n -> zoomOperator.zoom(n,zoomFactor,e.getSceneX(),e.getSceneY())
                    );
        });


        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

}
