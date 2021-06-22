package io.github.daomephsta.polar.common.entities.anomalies;

import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.api.components.IPolarChargeStorage;
import net.minecraft.nbt.NbtCompound;

public class AnomalyChargeStorage implements IPolarChargeStorage
{
    private final Polarity polarity;
    private int storedCharge;

    public AnomalyChargeStorage(AnomalyEntity anomaly, int initialCharge)
    {
        this.polarity = anomaly.getPolarity();
        this.storedCharge = initialCharge;
    }

    @Override
    public int charge(Polarity polarity, int maxAmount, boolean simulate)
    {
        if (!canCharge() || this.polarity != polarity) return maxAmount;
        int insertedCharge = Math.min(getMaxCharge() - storedCharge, maxAmount);
        if (!simulate)
            storedCharge += insertedCharge;
        return maxAmount - insertedCharge;
    }

    @Override
    public int discharge(Polarity polarity, int maxAmount, boolean simulate)
    {
        if (!canDischarge() || this.polarity != polarity) return 0;
        int extractedCharge = Math.min(storedCharge, maxAmount);
        if (!simulate)
            storedCharge -= extractedCharge;
        return extractedCharge;
    }

    @Override
    public int getStoredCharge()
    {
        return storedCharge;
    }

    @Override
    public Polarity getPolarity()
    {
        return polarity;
    }

    @Override
    public int getMaxCharge()
    {
        return 2000;
    }

    //Anomalies can only discharge
    @Override
    public boolean canCharge()
    {
        return false;
    }

    @Override
    public void readFromNbt(NbtCompound nbt)
    {
        this.storedCharge = nbt.getInt("stored_charge");
    }

    @Override
    public void writeToNbt(NbtCompound nbt)
    {
        nbt.putInt("stored_charge", storedCharge);
    }
}
