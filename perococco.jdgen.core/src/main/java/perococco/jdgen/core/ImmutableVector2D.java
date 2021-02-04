package perococco.jdgen.core;

import lombok.*;

@Value
public class ImmutableVector2D implements ROVector2D {

    public static @NonNull ImmutableVector2D of(double x, double y) {
        return new ImmutableVector2D(x, y);
    }

    double x;
    double y;

    public @NonNull ImmutableVector2D duplicate() {
        return this;
    }

    @Override
    public @NonNull ImmutableVector2D toImmutable() {
        return this;
    }

    public ImmutableVector2D normalized() {
        final var norm2 = x*x+y*y;
        if (norm2<=0) {
            return this;
        }
        final var norm = Math.sqrt(norm2);

        return of(x/norm, y/norm);
    }

    public @NonNull ImmutableVector2D add(@NonNull ImmutableVector2D other) {
        return of(x+other.x,y+other.y);
    }

    public @NonNull ImmutableVector2D add(double x, double y) {
        return of(x+this.x,y+this.y);
    }

    public @NonNull ImmutableVector2D addScaled(double x, double y, double scale) {
        return of(this.x+x*scale, this.y+y*scale);
    }

    public @NonNull ImmutableVector2D addScaled(@NonNull ImmutableVector2D other, double scale) {
        return of(this.x+other.x*scale,this.y+other.y*scale);
    }

    public @NonNull ImmutableVector2D subtract(@NonNull ImmutableVector2D other) {
        return of(this.x - other.x, this.y - other.y);
    }

    public @NonNull ImmutableVector2D scale(double scale) {
        return of(this.x*scale, this.y*scale);
    }

    public @NonNull ImmutableVector2D negate() {
        return of(-this.x, -this.y);
    }

    public double norm2() {
        return this.x*this.x+this.y*this.y;
    }

    @Override
    public String toString() {
        return "(" + x +", " + y +")";
    }
}
