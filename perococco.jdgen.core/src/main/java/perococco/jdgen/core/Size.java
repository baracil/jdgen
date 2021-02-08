package perococco.jdgen.core;

import lombok.NonNull;
import lombok.Value;

@Value
public class Size {

    int width;
    int height;

    public @NonNull Size addMargin(int margin) {
        return new Size(width+2*margin,height+2*margin);
    }

}
