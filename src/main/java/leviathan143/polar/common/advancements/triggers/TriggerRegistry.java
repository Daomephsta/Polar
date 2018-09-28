package leviathan143.polar.common.advancements.triggers;

import net.minecraft.advancements.CriteriaTriggers;

public class TriggerRegistry
{
	public static final TriggerPlayerAnomalyInteraction PLAYER_ANOMALY_INTERACTION = 
		CriteriaTriggers.register(new TriggerPlayerAnomalyInteraction());
	
	//Dummy method to force static init
	public static void init() {}
}
