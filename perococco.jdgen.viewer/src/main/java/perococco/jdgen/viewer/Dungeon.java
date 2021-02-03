package perococco.jdgen.viewer;

import com.google.common.collect.ImmutableMap;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import lombok.NonNull;
import perococco.jdgen.core.Rectangle;

import java.util.HashMap;
import java.util.Map;

public class Dungeon extends Group {

    private final ObjectProperty<ROGenerationModel> model = new SimpleObjectProperty<>();

    private final ObjectProperty<ImmutableMap<Rectangle,Color>> rectangles = new SimpleObjectProperty<>(ImmutableMap.of());

    private final InvalidationListener listener = l -> this.updateCells();

    public Dungeon() {
        this.model.addListener((l,o,n) -> onModelChanged(o,n));
        this.rectangles.addListener(l -> updateRooms());
    }

    public void setModel(@NonNull ROGenerationModel model) {
        this.model.set(model);
    }

    private void updateCells() {
        final var m = model.get();
        if (m == null) {
            this.rectangles.set(ImmutableMap.of());
            return;
        }

        final var cells = m.getCells();
        final var rooms = m.getRooms();

        final Map<Rectangle,Color> rectangles = new HashMap<>();
        cells.forEach(c -> rectangles.put(c,Color.BLUE));
        rooms.forEach(r -> rectangles.put(r.geometry(),Color.RED));
        this.rectangles.set(ImmutableMap.copyOf(rectangles));
    }

    private void onModelChanged(ROGenerationModel o, ROGenerationModel n) {
        if (o != null) {
            o.cellsProperty().removeListener(listener);
            o.roomsProperty().removeListener(listener);
        }
        if (n!=null) {
            n.cellsProperty().addListener(listener);
            n.roomsProperty().addListener(listener);
        }
        this.updateCells();
    }


    private void updateRooms() {
        this.synchronizeRooms(getChildren(), rectangles.get());
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
        rectangle.setHeight(cell.halfHeight()*2+1);
        rectangle.setWidth(cell.halfWidth()*2+1);
        rectangle.setX(cell.xc()-cell.halfWidth());
        rectangle.setY(cell.yc()-cell.halfHeight());
        rectangle.setStrokeWidth(0.01);
        rectangle.setStroke(Color.BLACK);
        rectangle.setFill(color);
        return rectangle;
    }
}
