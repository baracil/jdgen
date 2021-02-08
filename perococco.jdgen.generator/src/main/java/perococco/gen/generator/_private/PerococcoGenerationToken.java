package perococco.gen.generator._private;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import perococco.gen.generator.GenerationToken;
import perococco.jdgen.api.Map;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;

@RequiredArgsConstructor
public class PerococcoGenerationToken implements GenerationToken {

    @Delegate
    private final CompletionStage<Map> completableStage;

    private final Future<?> future;

    @Override
    public void stop() {
        future.cancel(true);
    }
}
