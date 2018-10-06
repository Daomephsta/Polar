package leviathan143.polar.common.advancements.triggers;

import net.minecraft.advancements.CriteriaTriggers;

public class TriggerRegistry
{
	public static final TriggerPlayerAnomalyInteraction PLAYER_ANOMALY_INTERACTION = 
		CriteriaTriggers.register(new TriggerPlayerAnomalyInteraction());
	public static final TriggerPolarReaction POLAR_REACTION =
		CriteriaTriggers.register(new TriggerPolarReaction());
	
	//Dummy method to force static init
	public static void init() {}
}
