package perococco.jdgen.viewer;

import javafx.beans.property.ReadOnlyObjectProperty;
import lombok.NonNull;

@FunctionalInterface
public interface ROGenerationModel {

    @NonNull ReadOnlyObjectProperty<ViewerState> stateProperty();

    default @NonNull ViewerState getState() {
        return stateProperty().get();
    }
}
