package perococco.gen.generator;

import java.util.concurrent.CompletionStage;

public interface GenerationToken extends CompletionStage<Map> {

    void stop();
}
