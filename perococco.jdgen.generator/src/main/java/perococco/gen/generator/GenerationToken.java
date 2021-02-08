package perococco.gen.generator;

import perococco.jdgen.api.Map;

import java.util.concurrent.CompletionStage;

public interface GenerationToken extends CompletionStage<Map> {

    void stop();
}
