package port;

import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.api.capabilities.IPolarChargeStorage;
import io.github.daomephsta.polar.common.capabilities.CapabilityPolarChargeable.SimplePolarChargeable;

@Deprecated
public class Dummy
{
	public static final IPolarChargeStorage CHARGE_STORAGE = new SimplePolarChargeable(Polarity.NONE, 0);
}
