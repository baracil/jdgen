package perococco.jdgen.viewer;

import com.google.common.collect.ImmutableList;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import lombok.NonNull;
import perococco.jdgen.core.Rectangle;

public class Dungeon extends Group {

    private final ObjectProperty<ImmutableList<Rectangle>> rooms = new SimpleObjectProperty<>(ImmutableList.of());

    public Dungeon() {
        this.rooms.addListener(l -> updateRooms());
    }

    public void bind(@NonNull ObservableValue<ImmutableList<Rectangle>> rooms) {
        this.rooms.bind(rooms);
    }

    private void updateRooms() {
        this.synchronizeRooms(getChildren(),rooms.get());
    }

    private void synchronizeRooms(@NonNull ObservableList<Node> children, @NonNull ImmutableList<Rectangle> cells) {
        final int nbNodes = children.size();
        final int nbRooms = cells.size();
        final int min = Math.min(nbNodes,nbRooms);

        for (int i = 0; i < min ; i++) {
            synchronizeRoom((javafx.scene.shape.Rectangle)children.get(i), cells.get(i));
        }
        if (nbNodes>nbRooms) {
            children.remove(min,nbNodes);
        } else if (nbRooms>nbNodes) {
            for (int i = min; i < nbRooms ; i++) {
                var node = synchronizeRoom(new javafx.scene.shape.Rectangle(), cells.get(i));
                children.add(node);
            }
        }
    }

    private javafx.scene.shape.Rectangle synchronizeRoom(@NonNull javafx.scene.shape.Rectangle rectangle, @NonNull Rectangle cell) {
        rectangle.setHeight(cell.height());
        rectangle.setWidth(cell.width());
        rectangle.setX(cell.x());
        rectangle.setY(cell.y());
        rectangle.setStroke(Color.BLACK);
        rectangle.setFill(Color.BLUE);
        return rectangle;
    }
}
