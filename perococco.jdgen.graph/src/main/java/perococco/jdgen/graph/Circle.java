package perococco.jdgen.graph;

import lombok.NonNull;
import lombok.Value;
import perococco.jdgen.core.ImmutableVector2D;
import perococco.jdgen.core.ROVector2D;

@Value
public class Circle {

    @NonNull ImmutableVector2D center;
    double radius;
    double radius2;

    public Circle(@NonNull ImmutableVector2D center, double radius) {
        this.center = center;
        this.radius = radius;
        this.radius2 = radius * radius;
    }

    public static Circle circumscribedCircleOf(@NonNull ROVector2D pa,
                                               @NonNull ROVector2D pb,
                                               @NonNull ROVector2D pc) {
        final var a2 = Tools.distance2(pa,pb);
        final var b2 = Tools.distance2(pb,pc);
        final var c2 = Tools.distance2(pc,pa);
        final var a = Math.sqrt(a2);
        final var b = Math.sqrt(b2);
        final var c = Math.sqrt(c2);

        final var s = 0.5*(a+b+c);
        final var radius = a*b*c/(4*Math.sqrt(s*(s-a)*(s-b)*(s-c)));

        final var id = 1./2*Tools.determinant2D(pa,pb,pc);

        final double x = id* Tools.determinant2D(a*a,pa.y(),b*b,pb.y(), c*c,pc.y());
        final double y = -id* Tools.determinant2D(a*a,pa.x(),b*b,pb.x(), c*c,pc.x());

        return new Circle(ImmutableVector2D.of(x,y),radius);
    }

    public boolean isInside(@NonNull ROVector2D point) {
        final var dx = point.x() - center.x();
        final var dy = point.y() - center.y();
        return dx * dx + dy * dy <= radius2;
    }
}
