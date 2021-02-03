package perococco.jdgen.core;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Vector2D {

    public static @NonNull Vector2D of(double x, double y) {
        return new Vector2D(x,y);
    }

    double x = 0.0;
    double y = 0.0;

    public @NonNull Vector2D duplicate() {
        return new Vector2D(x,y);
    }

    public @NonNull Vector2D setTo(@NonNull Vector2D other) {
        this.x=other.x;
        this.y=other.y;
        return this;
    }

    public @NonNull Vector2D setTo(double x, double y) {
        this.x=x;
        this.y=y;
        return this;
    }

    public Vector2D normalize() {
        final var norm2 = x*x+y*y;
        if (norm2<=0) {
            return this;
        }
        final var norm = Math.sqrt(norm2);

        this.x/=norm;
        this.y/=norm;
        return this;
    }

    public @NonNull Vector2D add(@NonNull Vector2D other) {
        this.x+=other.x;
        this.y+=other.y;
        return this;
    }

    public @NonNull Vector2D add(double x, double y) {
        this.x+=x;
        this.y+=y;
        return this;
    }

    public @NonNull Vector2D addScaled(double x, double y, double scale) {
        this.x+=x*scale;
        this.y+=y*scale;
        return this;
    }

    public @NonNull Vector2D addScaled(@NonNull Vector2D other, double scale) {
        this.x+=other.x*scale;
        this.y+=other.y*scale;
        return this;
    }

    public @NonNull Vector2D subtract(@NonNull Vector2D other) {
        this.x-=other.x;
        this.y-=other.y;
        return this;
    }

    public @NonNull Vector2D scale(double scale) {
        this.x*=scale;
        this.y*=scale;
        return this;
    }

    public @NonNull Vector2D negate() {
        this.x=-x;
        this.y=-y;
        return this;
    }

    public double norm2() {
        return this.x*this.x+this.y*this.y;
    }

    public Vector2D clipNorm(double maxSpeed) {
        if (norm2()<=maxSpeed*maxSpeed) {
            return this;
        }
        return normalize().scale(maxSpeed);
    }
}
