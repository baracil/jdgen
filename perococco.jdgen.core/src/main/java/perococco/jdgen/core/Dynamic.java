package perococco.jdgen.core;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class Dynamic {

    private final @NonNull Vector2D position = new Vector2D();
    private final @NonNull Vector2D velocity = new Vector2D();
    private final @NonNull Vector2D force = new Vector2D();

    public void update(double dt) {
        this.velocity.addScaled(force,dt).clipNorm(2);
        this.position.addScaled(velocity,dt);
    }
}
