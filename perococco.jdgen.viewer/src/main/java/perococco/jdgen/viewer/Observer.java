package perococco.jdgen.viewer;

import com.google.common.collect.ImmutableList;
import perococco.jdgen.core.Rectangle;

import java.util.function.Consumer;

@FunctionalInterface
public interface Observer extends Consumer<ImmutableList<Rectangle>> {
}
