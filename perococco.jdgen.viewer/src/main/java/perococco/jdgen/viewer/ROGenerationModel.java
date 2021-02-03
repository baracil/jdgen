package perococco.jdgen.viewer;

import com.google.common.collect.ImmutableList;
import javafx.beans.property.ReadOnlyObjectProperty;
import lombok.NonNull;
import perococco.jdgen.core.Rectangle;

public interface ROGenerationModel {

    @NonNull ReadOnlyObjectProperty<ImmutableList<Rectangle>> roomsProperty();

    default @NonNull ImmutableList<Rectangle> getRooms() {
        return roomsProperty().get();
    }
}
