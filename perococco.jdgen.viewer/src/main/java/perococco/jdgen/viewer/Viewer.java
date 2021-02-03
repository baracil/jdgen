package perococco.jdgen.viewer;

import com.google.common.collect.ImmutableList;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Pair;
import lombok.NonNull;
import perococco.jdgen.core.Rectangle;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Viewer extends Application {


    private static final Map<String,
            Pair<ObservableValue<ImmutableList<Rectangle>>,Thread>> PARAM = new HashMap<>();

    public static void launch(@NonNull ObservableValue<ImmutableList<Rectangle>> rooms, Thread t) {
        final var uuid = UUID.randomUUID();
        PARAM.put(uuid.toString(),new Pair<>(rooms,t));
        launch(uuid.toString());
    }

    private Dungeon dungeon = new Dungeon();

    private final ZoomOperator zoomOperator = new ZoomOperator(true);

    @Override
    public void start(Stage primaryStage) throws Exception {
        final var rooms = PARAM.remove(getParameters().getRaw().get(0));
        final var pane = new Pane();
        pane.getChildren().add(dungeon);
        rooms.getValue().start();
        dungeon.bind(rooms.getKey());
        final Scene scene = new Scene(pane,600,400);

        pane.setTranslateX(300);
        pane.setTranslateY(200);

        scene.setOnScroll(e -> {
            double zoomFactor = 1.5;
            if (e.getDeltaY() == 0) {
                return;
            }
            if (e.getDeltaY() < 0) {
                zoomFactor = 1./1.5;
            } if (e.getDeltaY() > 0) {
                zoomFactor = 1.5;
            }
            zoomOperator.zoom(pane,zoomFactor,e.getSceneX(),e.getSceneY());
        });



        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

}
