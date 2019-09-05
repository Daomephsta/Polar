package port;

import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.api.components.IPolarChargeStorage;
import io.github.daomephsta.polar.common.components.PolarChargeStorageComponent.SimplePolarChargeStorage;

@Deprecated
public class Dummy
{
	public static final IPolarChargeStorage CHARGE_STORAGE = new SimplePolarChargeStorage(Polarity.NONE, 0);
}
