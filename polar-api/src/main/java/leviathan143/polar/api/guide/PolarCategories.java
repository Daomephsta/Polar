package leviathan143.polar.api.guide;

import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.category.CategoryItemStack;
import amerifrance.guideapi.category.CategoryResourceLocation;
import leviathan143.polar.api.PolarAPI;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Provides access to Polar's guide categories.
 * @author Daomephsta
 */
public class PolarCategories
{
	public static final CategoryAbstract BASICS = new CategoryResourceLocation(createCategoryName("basics"), new ResourceLocation(PolarAPI.PROVIDER_MOD_ID, "textures/guide/misc/anomalies.png"));
	public static final CategoryAbstract COMBAT = new CategoryItemStack(createCategoryName("combat"), new ItemStack(Items.IRON_SWORD));
	public static final CategoryAbstract FARMING = new CategoryItemStack(createCategoryName("farming"), new ItemStack(Items.IRON_HOE));
	public static final CategoryAbstract BUILDING = new CategoryItemStack(createCategoryName("building"), new ItemStack(Blocks.BRICK_BLOCK));
	
	private static String createCategoryName(String name)
	{
		return PolarAPI.PROVIDER_MOD_ID + ".guide.category." + name + ".name";
	} 
}
