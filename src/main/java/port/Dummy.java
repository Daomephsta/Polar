package port;

import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.api.components.IPolarChargeStorage;
import io.github.daomephsta.polar.common.components.PolarChargeStorageComponent.Simple;

@Deprecated
public class Dummy
{
    public static final IPolarChargeStorage CHARGE_STORAGE = new Simple(Polarity.NONE, 0);
}
