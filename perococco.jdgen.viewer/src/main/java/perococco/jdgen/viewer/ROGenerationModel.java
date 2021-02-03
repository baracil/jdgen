package perococco.jdgen.viewer;

import com.google.common.collect.ImmutableList;
import javafx.beans.property.ReadOnlyObjectProperty;
import lombok.NonNull;
import perococco.jdgen.core.Rectangle;
import perococco.jdgen.core.Room;

public interface ROGenerationModel {

    @NonNull ReadOnlyObjectProperty<ImmutableList<Rectangle>> cellsProperty();

    @NonNull ReadOnlyObjectProperty<ImmutableList<Room>> roomsProperty();

    default @NonNull ImmutableList<Rectangle> getCells() {
        return cellsProperty().get();
    }

    default @NonNull ImmutableList<Room> getRooms() {
        return roomsProperty().get();
    }
}
