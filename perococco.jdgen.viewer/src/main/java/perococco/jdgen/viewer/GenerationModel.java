package perococco.jdgen.viewer;

import com.google.common.collect.ImmutableList;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.NonNull;
import perococco.jdgen.core.Rectangle;

public class GenerationModel implements ROGenerationModel {

    private final ObjectProperty<ImmutableList<Rectangle>> rooms = new SimpleObjectProperty<>(ImmutableList.of());

    public void setRooms(@NonNull ImmutableList<Rectangle> rooms) {
        this.rooms.set(rooms);
    }

    @Override
    public ImmutableList<Rectangle> getRooms() {
        return rooms.get();
    }

    @Override
    public ObjectProperty<ImmutableList<Rectangle>> roomsProperty() {
        return rooms;
    }
}
