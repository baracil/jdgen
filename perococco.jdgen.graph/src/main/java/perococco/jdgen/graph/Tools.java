package perococco.jdgen.graph;

import lombok.NonNull;
import perococco.jdgen.core.Point2D;

public class Tools {

    public static double distance(@NonNull Point2D p1, @NonNull Point2D p2) {
        return Math.sqrt(distance2(p1,p2));
    }

    public static double distance2(@NonNull Point2D p1, @NonNull Point2D p2) {
        final var dx = p1.getX() - p2.getX();
        final var dy = p1.getY() - p2.getY();
        return dx * dx + dy * dy;
    }

    public static double determinant2D(@NonNull Point2D pa, @NonNull Point2D pb, @NonNull Point2D pc) {
        return crossProduct(pb,pc) - crossProduct(pa,pc) + crossProduct(pa,pb);
    }

    public static double determinant2D(double xa, double ya, double xb, double yb, double xc, double yc) {
        return crossProduct(xb,yb,xc,yc) - crossProduct(xa,ya,xc,yc) + crossProduct(xa,ya,xb,yb);
    }

    private static double crossProduct(@NonNull Point2D v1, @NonNull Point2D v2) {
        return v1.getX() * v2.getY() - v1.getY() * v2.getX();
    }

    private static double crossProduct(double x1, double y1, double x2, double y2) {
        return x1*y2-x2*y1;
    }

}
