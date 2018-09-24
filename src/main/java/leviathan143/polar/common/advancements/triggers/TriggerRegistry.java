package leviathan143.polar.common.advancements.triggers;

import net.minecraft.advancements.CriteriaTriggers;

public class TriggerRegistry
{
	public static final PlayerAnomalyInteractionTrigger PLAYER_ANOMALY_INTERACTION = 
		CriteriaTriggers.register(new PlayerAnomalyInteractionTrigger());
	
	//Dummy method to force static init
	public static void init() {}
}
