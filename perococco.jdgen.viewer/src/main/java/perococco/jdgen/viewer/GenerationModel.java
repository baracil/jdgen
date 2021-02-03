package perococco.jdgen.viewer;

import com.google.common.collect.ImmutableList;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.NonNull;
import perococco.jdgen.core.Rectangle;
import perococco.jdgen.core.Room;

public class GenerationModel implements ROGenerationModel {

    private final ObjectProperty<ImmutableList<Rectangle>> cells = new SimpleObjectProperty<>(ImmutableList.of());
    private final ObjectProperty<ImmutableList<Room>> rooms = new SimpleObjectProperty<>(ImmutableList.of());

    public void setRooms(@NonNull ImmutableList<Room> rooms) {
        this.rooms.set(rooms);
    }

    public void setCells(ImmutableList<Rectangle> cells) {
        this.cells.set(cells);
    }

    @Override
    public ImmutableList<Rectangle> getCells() {
        return cells.get();
    }

    @Override
    public ObjectProperty<ImmutableList<Rectangle>> cellsProperty() {
        return cells;
    }

    @Override
    public ImmutableList<Room> getRooms() {
        return rooms.get();
    }

    @Override
    public ObjectProperty<ImmutableList<Room>> roomsProperty() {
        return rooms;
    }
}
