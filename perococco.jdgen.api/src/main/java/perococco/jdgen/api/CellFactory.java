package perococco.jdgen.api;

import lombok.NonNull;

import java.util.function.Function;
import java.util.function.IntFunction;

public interface CellFactory<C extends Cell> {

    @NonNull C createCell(@NonNull CellType cellType);

    @NonNull C[] arrayConstructor(int size);



    static <C extends Cell> @NonNull CellFactory<C> with(Function<? super CellType, ? extends C> factory, IntFunction<C[]> arrayConstructor) {
        return new CellFactory<C>() {
            @Override
            public @NonNull C createCell(@NonNull CellType cellType) {
                return factory.apply(cellType);
            }

            @Override
            public @NonNull C[] arrayConstructor(int size) {
                return arrayConstructor.apply(size);
            }
        };
    }
}
