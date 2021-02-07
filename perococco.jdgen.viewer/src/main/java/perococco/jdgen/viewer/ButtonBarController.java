package perococco.jdgen.viewer;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

import java.util.Random;

public class ButtonBarController {

    public Slider dungeonSize;
    public Slider minLength;
    public Slider ratio;
    public TextField seedValue;
    private GenerationManager generationManager;

    private final Random random = new Random();

    private BooleanProperty noSeed = new SimpleBooleanProperty(true);

    private final ChangeListener<String> listener = (l,o,n) -> this.onSeedChanged(n);

    private void onSeedChanged(String n) {
        System.out.println("N="+n);
        noSeed.set(false);
    }

    public void listenToSeed() {
        seedValue.textProperty().addListener(listener);
    }

    public void unlistenToSeed() {
        seedValue.textProperty().removeListener(listener);
    }

    public void initialize() {
        seedValue.clear();
        seedValue.styleProperty().bind(Bindings.createStringBinding(() -> noSeed.get()?"-fx-text-fill: gray":"",noSeed));
        this.listenToSeed();
    }

    public void setGenerationManager(GenerationManager generationManager) {
        this.generationManager = generationManager;
    }

    public void generate() {
        final long seed = getSeed();
        generationManager.generate(dungeonSize.getValue(), minLength.getValue(), minLength.getValue()*ratio.getValue(), seed);
    }

    private long getSeed() {
        long seed;
        if (noSeed.get()) {
            seed = generateRandomSeed();
        } else {
            seed = parseSeedFromTextField();
        }
        return seed;
    }

    private long parseSeedFromTextField() {
        try {
            return Long.parseLong(seedValue.getText(),16);
        } catch (NumberFormatException nfe) {
            return generateRandomSeed();
        }
    }

    private long generateRandomSeed() {
        var seed = random.nextLong();
        unlistenToSeed();
        seedValue.setText(Long.toHexString(seed));
        listenToSeed();
        return seed;
    }

    public void clearSeed(ActionEvent actionEvent) {
        unlistenToSeed();
        this.seedValue.clear();
        listenToSeed();
        this.noSeed.set(true);
    }
}
