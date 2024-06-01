package me.symmettry.papyrus.util.misc;

import lombok.experimental.UtilityClass;

import java.util.function.BiPredicate;

@UtilityClass
public final class PredUtil {

    public <T, U> BiPredicate<T, U> biPred(final boolean state) {
        return state ? PredUtil::trueBiPred : PredUtil::falseBiPred;
    }

    public <T, U> boolean falseBiPred(final T a, final U b) {
        return false;
    }
    public <T, U> boolean trueBiPred(final T a, final U b) {
        return true;
    }

}
