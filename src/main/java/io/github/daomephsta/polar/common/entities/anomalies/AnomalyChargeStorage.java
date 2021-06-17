package io.github.daomephsta.polar.common.entities.anomalies;

import io.github.daomephsta.polar.common.components.PolarChargeStorageComponent;

public class AnomalyChargeStorage extends PolarChargeStorageComponent.Simple
{
    public AnomalyChargeStorage(AnomalyEntity anomaly, int initialCharge)
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
