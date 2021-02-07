package perococco.jdgen.viewer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import lombok.NonNull;
import perococco.jdgen.core.Couple;
import perococco.jdgen.core.Rectangle;
import perococco.jdgen.core.Room;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Dungeon extends Group {

    private final ObjectProperty<ROGenerationModel> model = new SimpleObjectProperty<>();
    private final ObjectProperty<ImmutableMap<Rectangle,Color>> rectangles = new SimpleObjectProperty<>(ImmutableMap.of());
    private final ObjectProperty<ImmutableList<Couple<Room>>> graph = new SimpleObjectProperty<>(ImmutableList.of());

    private final InvalidationListener listener = l -> this.updateState();

    private final Group rectangleGroup = new Group();
    private final Group graphGroup = new Group();

    public Dungeon() {
        this.getChildren().addAll(rectangleGroup,graphGroup);
        this.model.addListener((l,o,n) -> onModelChanged(o,n));
        this.rectangles.addListener(l -> updateRooms());
        this.graph.addListener(l -> updateGraph());
    }

    public void setModel(@NonNull ROGenerationModel model) {
        this.model.set(model);
    }

    private void updateState() {
        final var m = model.get();
        if (m == null) {
            this.rectangles.set(ImmutableMap.of());
            return;
        }

        final var state = m.getState();

        final Map<Rectangle,Color> rectangles = new HashMap<>();
        state.getCells().forEach(c -> rectangles.put(c.getContainer(), Color.BLUE));
        state.getRooms().forEach(r -> rectangles.put(r.getContainer(), Color.RED));
        this.rectangles.set(ImmutableMap.copyOf(rectangles));

        if (state.getPath().isEmpty()) {
            this.graph.set(state.getDelaunayGraph());
        } else {
            this.graph.set(state.getPath());
        }
    }

    private void onModelChanged(ROGenerationModel o, ROGenerationModel n) {
        if (o != null) {
            o.stateProperty().removeListener(listener);
        }
        if (n!=null) {
            n.stateProperty().addListener(listener);
        }
        this.updateState();
    }


    private void updateGraph() {
        final var lines = graph.get().stream().map(this::toLine).collect(Collectors.toList());
        graphGroup.getChildren().setAll(lines);
    }

    private @NonNull Line toLine(@NonNull Couple<Room> edge) {
        final var state = model.get().getState();
        final var line = new Line();
        final var start = edge.getValue1().getPosition();
        final var end = edge.getValue2().getPosition();
        line.setStroke(Color.rgb(0,255,0));
        line.setStrokeWidth(state.getMinRoomSize()/4.);
        line.setStartX(start.getX());
        line.setStartY(start.getY());
        line.setEndX(end.getX());
        line.setEndY(end.getY());
        return line;
    }




    private void updateRooms() {
        this.synchronizeRooms(rectangleGroup.getChildren(), rectangles.get());
    }

    private void synchronizeRooms(@NonNull ObservableList<Node> children, @NonNull ImmutableMap<Rectangle,Color> rectangles) {
        final int nbNodes = children.size();
        final int nbRectangles = rectangles.size();
        final int min = Math.min(nbNodes,nbRectangles);


        int i = 0;
        for (Map.Entry<Rectangle, Color> entry : rectangles.entrySet()) {
            if (i<min) {
                synchronizeRoom((javafx.scene.shape.Rectangle)children.get(i),entry.getKey(),entry.getValue());
            } else {
                var node = synchronizeRoom(new javafx.scene.shape.Rectangle(), entry.getKey(), entry.getValue());
                children.add(node);
            }
            i++;
        }

        if (nbNodes>nbRectangles) {
            children.remove(min,nbNodes);
        }
    }

    private javafx.scene.shape.Rectangle synchronizeRoom(@NonNull javafx.scene.shape.Rectangle rectangle, @NonNull Rectangle cell, @NonNull Color color) {
        rectangle.setHeight(cell.getHalfHeight()*2+1);
        rectangle.setWidth(cell.getHalfWidth()*2+1);
        rectangle.setX(cell.getXc()-cell.getHalfWidth());
        rectangle.setY(cell.getYc()-cell.getHalfHeight());
        rectangle.setStrokeWidth(0.01);
        rectangle.setStroke(Color.BLACK);
        rectangle.setFill(color);
        return rectangle;
    }
}
