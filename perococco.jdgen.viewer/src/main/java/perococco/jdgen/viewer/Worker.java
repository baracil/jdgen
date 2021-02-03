package perococco.jdgen.viewer;

import com.google.common.collect.ImmutableList;
import javafx.beans.value.ObservableValue;
import lombok.NonNull;
import perococco.jdgen.core.Rectangle;

public interface Worker extends Runnable {

    @NonNull ObservableValue<ImmutableList<Rectangle>> observableRectangles();

}
