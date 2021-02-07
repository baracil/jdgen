package perococco.jdgen.graph;

import lombok.NonNull;
import lombok.Value;
import perococco.jdgen.core.Point2D;

@Value
public class Circle {

    @NonNull Point2D center;
    double radius;
    double radius2;

    public Circle(@NonNull Point2D center, double radius) {
        this.center = center;
        this.radius = radius;
        this.radius2 = radius * radius;
    }

    public static Circle circumscribedCircleOf(@NonNull Point2D pa,
                                               @NonNull Point2D pb,
                                               @NonNull Point2D pc) {
        final var a = Tools.distance(pa,pb);
        final var b = Tools.distance(pb,pc);
        final var c = Tools.distance(pc,pa);

        final var da2 = pa.distanceToOriginSquared();
        final var db2 = pb.distanceToOriginSquared();
        final var dc2 = pc.distanceToOriginSquared();

        final var p = 0.5*(a+b+c);
        final var radius = a*b*c/(4*Math.sqrt(p*(p-a)*(p-b)*(p-c)));

        final var id = 1./(2*Tools.determinant2D(pa,pb,pc));

        final double x =  id* Tools.determinant2D(da2,pa.getY(),db2,pb.getY(), dc2,pc.getY());
        final double y = -id* Tools.determinant2D(da2,pa.getX(),db2,pb.getX(), dc2,pc.getX());

        return new Circle(Point2D.of(x, y), radius);
    }

    public boolean isInside(@NonNull Point2D point) {
        final var dx = point.getX() - center.getX();
        final var dy = point.getY() - center.getY();
        return dx * dx + dy * dy <= radius2;
    }
}
