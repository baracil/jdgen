package perococco.jdgen.core;

import lombok.NonNull;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface IntPoint {

    int getX();
    int getY();

    default Stream<IntPoint> neighbours() {
        final int x = getX();
        final int y = getY();
        return Stream.of(
                new IntVector(x - 1, y - 1),
                new IntVector(x - 1, y),
                new IntVector(x - 1, y + 1),
                new IntVector(x, y - 1),
                new IntVector(x, y + 1),
                new IntVector(x + 1, y - 1),
                new IntVector(x + 1, y),
                new IntVector(x + 1, y + 1)
        );
    }

    default @NonNull IntPoint translate(int xoffset,int yoffset) {
        return new IntVector(getX()+xoffset,getY()+yoffset);
    }

    default @NonNull Stream<IntPoint> leftNeighbours() {
        final int x = getX();
        final int y = getY();
        return IntStream.rangeClosed(y-1,y+1).mapToObj(yy -> new IntVector(x-1,yy));
    }

    default @NonNull Stream<IntPoint> rightNeighbours() {
        final int x = getX();
        final int y = getY();
        return IntStream.rangeClosed(y-1,y+1).mapToObj(yy -> new IntVector(x+1,yy));
    }

    default @NonNull Stream<IntPoint> upperNeighbours() {
        final int x = getX();
        final int y = getY();
        return IntStream.rangeClosed(x-1,x+1).mapToObj(xx -> new IntVector(xx,y+1));
    }

    default @NonNull Stream<IntPoint> lowerNeighbours() {
        final int x = getX();
        final int y = getY();
        return IntStream.rangeClosed(x-1,x+1).mapToObj(xx -> new IntVector(xx,y-1));
    }

    default @NonNull IntPoint left() {
        return new IntVector(getX()-1,getY());
    }

    default @NonNull IntPoint right() {
        return new IntVector(getX()+1,getY());
    }

    default @NonNull IntPoint above() {
        return new IntVector(getX(),getY()+1);
    }

    default @NonNull IntPoint below() {
        return new IntVector(getX(),getY()-1);
    }

}
