package perococco.jdgen.viewer;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.util.Duration;

public class ZoomOperator {

    private final Timeline timeline;

    private final boolean noAnimation;

    public ZoomOperator(boolean noAnimation) {
        this.noAnimation = noAnimation;
        this.timeline = new Timeline(60);
    }

    public ZoomOperator() {
        this(false);
    }

    public void zoom(Node node, double factor, double x, double y) {
        // determine scale
        double oldScale = node.getScaleX();
        double scale = oldScale * factor;
        double f = (scale / oldScale) - 1;

        // determine offset that we will have to move the node
        Bounds bounds = node.localToScene(node.getBoundsInLocal());
        double dx = (x - (bounds.getWidth() / 2 + bounds.getMinX()));
        double dy = (y - (bounds.getHeight() / 2 + bounds.getMinY()));

        if (noAnimation) {

            node.translateXProperty().set(node.getTranslateX() - f * dx);
            node.translateYProperty().set(node.getTranslateY() - f * dy);
            node.scaleXProperty().set(scale);
            node.scaleYProperty().set(scale);
        } else {

            //       timeline that scales and moves the node
            timeline.getKeyFrames().clear();
            timeline.getKeyFrames().addAll(
                    new KeyFrame(Duration.millis(200), new KeyValue(node.translateXProperty(), node.getTranslateX() - f * dx)),
                    new KeyFrame(Duration.millis(200), new KeyValue(node.translateYProperty(), node.getTranslateY() - f * dy)),
                    new KeyFrame(Duration.millis(200), new KeyValue(node.scaleXProperty(), scale)),
                    new KeyFrame(Duration.millis(200), new KeyValue(node.scaleYProperty(), scale))
            );
            timeline.play();
        }
    }
}
