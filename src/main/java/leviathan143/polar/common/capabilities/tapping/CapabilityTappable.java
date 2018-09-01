package leviathan143.polar.common.capabilities.tapping;

import daomephsta.umbra.capabilities.CapabilityHelper;
import leviathan143.polar.api.capabilities.ITappable;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityTappable
{
	public static void register()
	{
		CapabilityManager.INSTANCE.register(ITappable.class, CapabilityHelper.noOpStorage(), () -> null);
	}
}
