package perococco.jdgen.core;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
public class NormalDistribution {

    private final @NonNull Random random;
    private final double mean;
    private final double std;

    private Double spare = null;

    @Synchronized
    public double sample() {
        if (spare != null) {
            var val = spare;
            spare = null;
            return val*std+mean;
        } else {
            double u,v,s;
            do {
                u = random.nextDouble()*2-1;
                v = random.nextDouble()*2-1;
                s = u*u + v*v;
            } while (s>=1 || s == 0);
            s = Math.sqrt(-2*Math.log(s)/s);
            spare = v*s;
            return mean+std*u*s;
        }
    }

}
