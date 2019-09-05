package io.github.daomephsta.polar.common.entities.anomalies;

import io.github.daomephsta.polar.common.capabilities.CapabilityPolarChargeable;

public class AnomalyChargeStorage extends CapabilityPolarChargeable.SimplePolarChargeable
{
	public AnomalyChargeStorage(EntityAnomaly anomaly, int initialCharge)
	{
		super(anomaly.getPolarity(), 2000, initialCharge);
		
	}
	
	//Anomalies can only discharge
	@Override
	public boolean canCharge()
	{
		return false;
	}
}
