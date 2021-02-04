package perococco.jdgen.viewer;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.NonNull;

public class GenerationModel implements ROGenerationModel {

    private final ObjectProperty<ViewerState> state = new SimpleObjectProperty<>(ViewerState.initial());

    @Override
    public @NonNull ObjectProperty<ViewerState> stateProperty() {
        return state;
    }

    public void setState(@NonNull ViewerState state) {
        this.state.set(state);
    }
}
