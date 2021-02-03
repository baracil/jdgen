package perococco.jdgen.rooms;

import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.jdgen.core.JDGenConfiguration;
import perococco.jdgen.core.Rectangle;

import java.util.function.Consumer;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CellsSeparator {

    public static final int MAX_ROOM_ROUND_STEP_AMOUNT = 30;

    public static @NonNull ImmutableList<Rectangle> separate(@NonNull JDGenConfiguration configuration,
                                                             @NonNull ImmutableList<Rectangle> cells,
                                                             @NonNull Consumer<ImmutableList<Rectangle>> observer) {
        return new CellsSeparator(configuration,cells, observer).separate();
    }

    private final @NonNull JDGenConfiguration configuration;
    private final @NonNull ImmutableList<Rectangle> input;
    private final @NonNull Consumer<ImmutableList<Rectangle>> observer;


    private Rectangle[] cells;


    private @NonNull ImmutableList<Rectangle> separate() {
        this.cells = input.toArray(Rectangle[]::new);

        var radialStep = 0.1 + Math.max(1 - configuration.density(), 0) * configuration.maxRoomSize() * 0.9;
        var maxR = input.size()*configuration.maxRoomSize()*2;

        for (int i = 1; i < cells.length; i++) {
            final var roomAngle = Math.random()*2*Math.PI;
            final var dirX = Math.cos(roomAngle)*radialStep;
            final var dirY = Math.sin(roomAngle)*radialStep;
            var posX = 0.;
            var posY = 0.;

            for (int k = 0; k < maxR; k++) {
                posX+=dirX;
                posY+=dirY;

                cells[i] = cells[i].withPos(posX,posY);

                boolean colliding = collides(i);
                if (!colliding) {
                    double d= Math.sqrt(posX*posX+posY*posY);
                    double curAngle = roomAngle;
                    final var maxStepsAmount = Math.min(d,MAX_ROOM_ROUND_STEP_AMOUNT);
                    final var angleStep = 1./maxStepsAmount+(1-configuration.density())*(2*Math.PI-1./maxStepsAmount);

                    while (curAngle - roomAngle<Math.PI*2) {
                        curAngle+=angleStep;
                        for (double ii = d; ii > 0 ; ii-=radialStep) {
                            cells[i] = cells[i].withPos(ii*Math.cos(curAngle),ii*Math.sin(curAngle));
                            if (!collides(i)) {
                                d=ii;
                                posX = cells[i].x();
                                posY = cells[i].y();
                            }
                        }
                    }

                    cells[i] = cells[i].withPos(posX,posY);
                    break;
                }

            }
            sleep();
            observer.accept(ImmutableList.copyOf(cells));
        }

        return ImmutableList.copyOf(cells);
    }

    private boolean collides(int source) {
        for (int j = 0; j < source; j++) {
            if (cells[source].overlap(cells[j])) {
                return true;
            }
        }
        return false;
    }

    private void sleep() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }

}
