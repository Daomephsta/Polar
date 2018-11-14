package leviathan143.polar.common.handlers.baubles;

import baubles.api.BaublesApi;
import baubles.api.IBauble;
import leviathan143.polar.common.Polar;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.IItemHandler;

@Mod.EventBusSubscriber(modid = Polar.MODID)
public class BaubleHandler
{
	public static void preInit()
	{
		
	}
	
	@SubscribeEvent
	public static void handleBlockBreak(BlockEvent.BreakEvent event)
	{
		FallingBlockStabiliserHandler.stabiliseFallingBlocks(event);
		FallingBlockDestroyerHandler.destroyFallingBlocks(event);
	}
	
	static ItemStack findEquippedBauble(EntityPlayer player, IBauble bauble)
	{
		IItemHandler baubles = BaublesApi.getBaublesHandler(player);
		for (int s = 0; s < baubles.getSlots(); s++)
		{
			ItemStack stack = baubles.getStackInSlot(s);
			if (stack.getItem() == bauble)
				return stack;
		}
		return ItemStack.EMPTY;
	}
}
