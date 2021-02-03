package perococco.jdgen.core;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

import java.util.Comparator;

@Value
@EqualsAndHashCode(of = {"xc","yc","halfHeight","halfWidth"})
public class Rectangle {

    public static final Comparator<Rectangle> DISTANCE_COMPARATOR = Comparator.comparingDouble(Rectangle::distance);

    int xc;
    int yc;

    int halfWidth;
    int halfHeight;

    double distance;

    public Rectangle(int xc, int yc, int halfWidth, int halfHeight) {
        this.xc = xc;
        this.yc = yc;
        this.halfWidth = halfWidth;
        this.halfHeight = halfHeight;
        this.distance = Math.sqrt(xc * xc + yc * yc);
    }

    public boolean overlap(@NonNull Rectangle other) {
        boolean xOverlap = Math.abs(other.xc - xc) <= (other.halfWidth + this.halfWidth);
        boolean yOverlap = Math.abs(other.yc - yc) <= (other.halfHeight + this.halfHeight);
        return xOverlap && yOverlap;
    }

    public int displacementToPutRightOf(@NonNull Rectangle reference) {
        return (reference.xc - this.xc) + (reference.halfWidth + this.halfWidth + 1);
    }

    public int displacementToPutLeftOf(@NonNull Rectangle reference) {
        return (reference.xc - this.xc) - (reference.halfWidth + this.halfWidth + 1);
    }

    public int displacementToPutAboveOf(@NonNull Rectangle reference) {
        return (reference.yc - this.yc) - (reference.halfHeight + this.halfHeight + 1);
    }

    public int displacementToPutBelowOf(@NonNull Rectangle reference) {
        return (reference.yc - this.yc) + (reference.halfHeight + this.halfHeight + 1);
    }


    public @NonNull Rectangle withPos(double posX, double posY) {
        return new Rectangle((int) Math.round(posX), (int) Math.round(posY), halfWidth,halfHeight);
    }

    public @NonNull Rectangle translate(IntVector displacement) {
        return new Rectangle(xc + displacement.x(), yc + displacement.y(), halfWidth,halfHeight);
    }
}
