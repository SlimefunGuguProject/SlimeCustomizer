package net.guizhanss.slimecustomizer.utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;

public final class NumberUtils {
    public static Optional<Integer> getConfigInt(@Nullable String original, @Nonnull Function<Integer, Boolean> validator) {
        int value;
        try {
            value = Integer.parseInt(original);
        } catch (NullPointerException | NumberFormatException ex) {
            return Optional.empty();
        }

        if (validator.apply(value)) {
            return Optional.of(value);
        } else {
            return Optional.empty();
        }
    }
}
