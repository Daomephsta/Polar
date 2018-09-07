package leviathan143.polar.api.guide;

import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.category.CategoryItemStack;
import leviathan143.polar.api.PolarAPI;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * Provides access to Polar's guide categories.
 * @author Daomephsta
 */
public class PolarCategories
{
	public static final String CATEGORY_PREFIX = PolarAPI.PROVIDER_MOD_ID + ".guide.category.";
	public static final CategoryAbstract BASICS = createItemStackCategory("basics", new ItemStack(Blocks.LOG));
	public static final CategoryAbstract COMBAT = createItemStackCategory("combat", new ItemStack(Items.IRON_SWORD));
	public static final CategoryAbstract FARMING = createItemStackCategory("farming", new ItemStack(Items.IRON_HOE));
	public static final CategoryAbstract BUILDING = createItemStackCategory("building", new ItemStack(Blocks.BRICK_BLOCK));
	
	private static CategoryAbstract createItemStackCategory(String name, ItemStack stack)
	{
		return new CategoryItemStack(createCategoryName(name), stack).withKeyBase(PolarAPI.PROVIDER_MOD_ID);
	}

	private static String createCategoryName(String name)
	{
		return CATEGORY_PREFIX + name + ".name";
	} 
}
