package perococco.jdgen.core;

import lombok.Value;

@Value
public class JDGenConfiguration {

    /**
     * The seed to use to generate the dungeon
     */
    long seed;

    /**
     * The size of the dungeon. This is not the number of cells, just a indication
     * of the size.
     */
    int dungeonSize;

    /**
     * The minimal cell size
     */
    int minRoomSize;

    /**
     * The maximal cell size
     */
    int maxRoomSize;

    /**
     * A threshold used to select the main room from the cells.
     * The higher the lower the number of main room (generally 1.25 is ok)
     */
    double mainRoomThreshold;

}
