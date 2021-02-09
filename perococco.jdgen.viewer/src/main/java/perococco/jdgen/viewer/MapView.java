package perococco.jdgen.viewer;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.NonNull;
import perococco.jdgen.api.Cell;

import java.util.Random;

public class MapView extends Canvas {

    private final ObjectProperty<ROGenerationModel> model = new SimpleObjectProperty<>();

    private final InvalidationListener listener = l -> this.updateCanvas();

    public MapView() {
        this.model.addListener((l,o,n) -> onModelChanged(o,n));
        this.widthProperty().addListener(l -> updateCanvas());
        this.heightProperty().addListener(l -> updateCanvas());
    }


    public void setModel(@NonNull ROGenerationModel model) {
        this.model.set(model);
    }



    private void onModelChanged(ROGenerationModel o, ROGenerationModel n) {
        if (o != null) {
            o.stateProperty().removeListener(listener);
        }
        if (n!=null) {
            n.stateProperty().addListener(listener);
        }
        this.updateCanvas();
    }


    private void updateCanvas() {
        final var context = this.getGraphicsContext2D();
        final var w = this.getWidth();
        final var h = this.getHeight();


        final var map = this.model.get().getState().map().orElse(null);
        if (map == null) {
            context.clearRect(0,0,w,h);
            return;
        }

        final var size = map.getSize();

        final var sizeX = w/size.getWidth();
        final var sizeY = h/size.getHeight();

        final var cellSize = Math.min(sizeX,sizeY);

        for (int x = 0; x < size.getWidth(); x++) {
            for(int y = 0; y<size.getHeight();y++) {
                var cell = map.getCellAt(x,y);
                drawCell(context,x,y,cell,cellSize);
            }
         }
    }

    private final static Color[] COLORS = {
            Color.RED,
            Color.GAINSBORO,
            Color.BLUE
    };

    private final static Random RANDOM = new Random();

    private void drawCell(@NonNull GraphicsContext context, int x, int y, Cell cell, double cellSize) {
        final var color = getCellColor(cell);
        context.setFill(color);
        context.fillRect(x*cellSize,y*cellSize,cellSize,cellSize);
    }

    private Color getCellColor(@NonNull Cell cell) {
        return switch (cell.getType()) {
            case EMPTY -> Color.WHITE;
            case DOOR -> Color.GOLD;
            case CELL_FLOOR -> Color.BLUE;
            case ROOM_FLOOR -> Color.RED;
            case CORRIDOR_FLOOR -> Color.GREEN;
            case WALL -> Color.BLACK;
        };
    }


}
