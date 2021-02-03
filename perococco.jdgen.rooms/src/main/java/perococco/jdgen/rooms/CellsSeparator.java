package perococco.jdgen.rooms;

import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.Dynamic;
import perococco.jdgen.core.JDGenConfiguration;
import perococco.jdgen.core.Rectangle;
import perococco.jdgen.core.Vector2D;

import java.util.Arrays;
import java.util.function.Consumer;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CellsSeparator {

    public static ImmutableList<Rectangle> separate(@NonNull JDGenConfiguration configuration,
                                                    @NonNull ImmutableList<Rectangle> cells, @NonNull Consumer<ImmutableList<Rectangle>> observer) {
        return new CellsSeparator(configuration,cells, observer).separate();
    }


    private final JDGenConfiguration configuration;
    private final ImmutableList<Rectangle> cells;

    private final Consumer<ImmutableList<Rectangle>> observer;

    private ImmutableList<Entity> entities;

    private ImmutableList<Rectangle> separate() {
        var anyColliding = false;
        int nbIterations = 0;

        this.entities = this.cells.stream().map(Entity::new).collect(ImmutableList.toImmutableList());
        do {
            observer.accept(getFinalCells());
            entities.forEach(e -> e.dynamic.velocity().setTo(0,0));
            anyColliding = computeSeparation();
            computeAgglomeration();
            entities.forEach(e -> e.update(10./configuration.maxRoomSize()));
            observer.accept(getFinalCells());
            if (anyColliding) {
                nbIterations++;
            }
        } while (!Thread.currentThread().isInterrupted() && (anyColliding || nbIterations<200));
        System.out.println(nbIterations);
        return getFinalCells();
    }

    private void computeAgglomeration() {
        var com = new Vector2D();
        entities.forEach(e -> com.add(e.dynamic.position()));
        com.scale(1./entities.size());
        entities.stream()
                .filter(e -> !e.colliding)
                .forEach(e -> {
            var force = com.duplicate()
                           .subtract(e.dynamic.position())
                           .scale(0.00001)
                           .clipNorm(0.5);
            e.dynamic.force().add(force);
        });
    }

    private void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public ImmutableList<Rectangle> getFinalCells() {
        return entities.stream().map(Entity::finalCell).collect(ImmutableList.toImmutableList());
    }

    private boolean computeSeparation() {
        var buffer = new Vector2D();
        var someCollide = false;
        for (int i = 0; i < entities.size(); i++) {
            var ei = entities.get(i);
            ei.colliding = false;
            ei.dynamic.force().scale(0);
            for (int j = 0; j < entities.size(); j++) {
                if (i == j) {
                    continue;
                }

                var ej = entities.get(j);

                if (ei.collide(ej,0)) {
                    someCollide = true;
                    ei.colliding = true;
                }
                if (ei.collide(ej,1.5)) {
                    buffer.setTo(ej.dynamic.position()).subtract(ei.dynamic.position());
//                    buffer.x(buffer.x()+Math.random()*0.2-0.1);
//                    buffer.y(buffer.y()+Math.random()*0.2-0.1);
                    buffer.scale(-0.5);
                    ei.dynamic.force().add(buffer);
                }
            }
            if (!ei.colliding) {
                ei.dynamic.velocity().scale(0);
            }
        }
        return someCollide;
    }


    private static class Entity {

        private final double halfWidth;
        private final double halfHeight;

        private boolean colliding;

        private final Dynamic dynamic = new Dynamic();

        public Entity(@NonNull Rectangle rectangle) {
            this.halfHeight = rectangle.halfHeight();
            this.halfWidth = rectangle.halfWidth();
            this.dynamic.position().setTo(rectangle.xc(), rectangle.yc());
        }

        public boolean collide(@NonNull Entity that,double margin) {
            var ox = overlap(
                    this.x(), this.halfWidth,
                    that.x(), that.halfWidth,margin);
            var oy = overlap(
                    this.y(), this.halfHeight,
                    that.y(), that.halfHeight,margin);
            return ox && oy;
        }

        public double x() {
            return (dynamic).position().x();
        }

        public double y() {
            return (dynamic).position().y();
        }

        public void update(double dt) {
            this.dynamic.force().scale(halfHeight*halfWidth);
            this.dynamic.update(dt);
//            this.dynamic.position().x(Math.round(this.dynamic.position().x()));
//            this.dynamic.position().y(Math.round(this.dynamic.position().y()));
        }

        public Rectangle finalCell() {
            return new Rectangle((int)Math.round(x()), (int)Math.round(y()), (int) Math.round(halfWidth), (int) Math.round(halfHeight));
        }
    }

    private static boolean overlap(double x1, double hl1, double x2, double hl2,double margin) {
        return Math.abs(Math.round(x1) - Math.round(x2)) <= (hl1 + hl2+margin);
    }
}
