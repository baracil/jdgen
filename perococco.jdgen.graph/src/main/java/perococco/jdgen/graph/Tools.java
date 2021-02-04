package perococco.jdgen.graph;

import lombok.NonNull;
import perococco.jdgen.core.Point2D;

public class Tools {

    public static double sign(@NonNull Point2D p1, @NonNull Point2D p2, @NonNull Point2D p3) {
        return (p1.x() - p3.x()) * (p2.y() - p3.y()) - (p2.x() - p3.x()) * (p1.y() - p3.y());
    }

    public static double distance(@NonNull Point2D p1, @NonNull Point2D p2) {
        return Math.sqrt(distance2(p1,p2));
    }

    public static double distance2(@NonNull Point2D p1, @NonNull Point2D p2) {
        final var dx = p1.x() - p2.x();
        final var dy = p1.y() - p2.y();
        return dx * dx + dy * dy;
    }

    public static double determinant2D(@NonNull Point2D pa, @NonNull Point2D pb, @NonNull Point2D pc) {
        return crossProduct(pb,pc) - crossProduct(pa,pc) + crossProduct(pa,pb);
    }

    public static double determinant2D(double xa, double ya, double xb, double yb, double xc, double yc) {
        return crossProduct(xb,yb,xc,yc) - crossProduct(xa,ya,xc,yc) + crossProduct(xa,ya,xb,yb);
    }

    public static double crossProduct(@NonNull Point2D v1, @NonNull Point2D v2) {
        return v1.x() * v2.y() - v1.y() * v2.x();
    }

    public static double crossProduct(double x1, double y1, double x2, double y2) {
        return x1*y2-x2*y1;
    }


    public static boolean isInTriangle(@NonNull Point2D pt, @NonNull Triangle triangle) {
        final double d1, d2, d3;
        final boolean has_neg, has_pos;

        d1 = sign(pt, triangle.vertex1(), triangle.vertex2());
        d2 = sign(pt, triangle.vertex2(), triangle.vertex3());
        d3 = sign(pt, triangle.vertex3(), triangle.vertex1());

        has_neg = (d1 < 0) || (d2 < 0) || (d3 < 0);
        has_pos = (d1 > 0) || (d2 > 0) || (d3 > 0);

        return !(has_neg && has_pos);
    }
}
