package leviathan143.polar.common.items.blue;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import daomephsta.umbra.scheduler.ActionRegistry;
import daomephsta.umbra.scheduler.Scheduler;
import leviathan143.polar.common.items.TestAction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BaubleFallingBlockDestroyer extends Item implements IBauble
{
	public BaubleFallingBlockDestroyer()
	{
		ActionRegistry.register(new TestAction.TestActionSerDes());
	}
	
	@Override
	public BaubleType getBaubleType(ItemStack itemstack)
	{
		return BaubleType.TRINKET;
	}
}
