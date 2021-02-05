package perococco.jdgen.core;

import lombok.Value;

@Value
public class JDGenConfiguration {

    long seed;

    int dungeonSize;

    int minRoomSize;

    int maxRoomSize;

    double mainRoomThreshold;

}
