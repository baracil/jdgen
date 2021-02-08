package perococco.gen.generator;

import lombok.NonNull;
import perococco.gen.generator._private.BasicIntPoint;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface IntPoint {

    int getX();
    int getY();

    default Stream<IntPoint> neighbours() {
        final int x = getX();
        final int y = getY();
        return Stream.of(
                new BasicIntPoint(x - 1, y - 1),
                new BasicIntPoint(x - 1, y),
                new BasicIntPoint(x - 1, y + 1),
                new BasicIntPoint(x, y - 1),
                new BasicIntPoint(x, y + 1),
                new BasicIntPoint(x + 1, y - 1),
                new BasicIntPoint(x + 1, y),
                new BasicIntPoint(x + 1, y + 1)
        );
    }

    default @NonNull IntPoint translate(int xoffset,int yoffset) {
        return new BasicIntPoint(getX()+xoffset,getY()+yoffset);
    }

    default @NonNull Stream<IntPoint> leftNeighbours() {
        final int x = getX();
        final int y = getY();
        return IntStream.rangeClosed(y-1,y+1).mapToObj(yy -> new BasicIntPoint(x-1,yy));
    }

    default @NonNull Stream<IntPoint> rightNeighbours() {
        final int x = getX();
        final int y = getY();
        return IntStream.rangeClosed(y-1,y+1).mapToObj(yy -> new BasicIntPoint(x+1,yy));
    }

    default @NonNull Stream<IntPoint> upperNeighbours() {
        final int x = getX();
        final int y = getY();
        return IntStream.rangeClosed(x-1,x+1).mapToObj(xx -> new BasicIntPoint(xx,y+1));
    }

    default @NonNull Stream<IntPoint> lowerNeighbours() {
        final int x = getX();
        final int y = getY();
        return IntStream.rangeClosed(x-1,x+1).mapToObj(xx -> new BasicIntPoint(xx,y-1));
    }

    default @NonNull IntPoint left() {
        return new BasicIntPoint(getX()-1,getY());
    }

    default @NonNull IntPoint right() {
        return new BasicIntPoint(getX()+1,getY());
    }

    default @NonNull IntPoint above() {
        return new BasicIntPoint(getX(),getY()+1);
    }

    default @NonNull IntPoint below() {
        return new BasicIntPoint(getX(),getY()-1);
    }

}
