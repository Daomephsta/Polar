package leviathan143.polar.common.guide;

import amerifrance.guideapi.api.GuideBook;
import amerifrance.guideapi.api.IGuideBook;
import amerifrance.guideapi.api.impl.*;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.category.CategoryItemStack;
import amerifrance.guideapi.page.PageFurnaceRecipe;
import leviathan143.polar.api.PolarGuideConstructionEvent;
import leviathan143.polar.common.Polar;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

@GuideBook
public class BookResearchJournal implements IGuideBook
{
	public static Book BOOK_INSTANCE = null;

	private static final String CATEGORY_PREFIX = Polar.MODID + ".guide.category.";

	private static class Categories
	{
		private static final String BASICS = "basics";
		private static final String COMBAT = "combat";
		private static final String FARMING = "farming";
		private static final String BUILDING = "building";
	}

	@Override
	public Book buildBook()
	{
		// Setup the book
		BookBinder builder = new BookBinder(new ResourceLocation(Polar.MODID, "research_journal"));
		builder.setGuideTitle(Polar.MODID + ".guide.title");
		builder.setItemName(Polar.MODID + ".guide.name");
		builder.setHeader("");

		// Add categories, entries and pages
		CategoryAbstract basics = new CategoryItemStack(createCategoryName(Categories.BASICS), new ItemStack(Blocks.LOG)).withKeyBase(Polar.MODID);
		{
		}
		builder.addCategory(basics);
		CategoryAbstract combat = new CategoryItemStack(createCategoryName(Categories.COMBAT), new ItemStack(Items.IRON_SWORD)).withKeyBase(Polar.MODID);
		{
		}
		builder.addCategory(combat);
		CategoryAbstract farming = new CategoryItemStack(createCategoryName(Categories.FARMING), new ItemStack(Items.IRON_HOE)).withKeyBase(Polar.MODID);
		{
		}
		builder.addCategory(farming);
		CategoryAbstract building = new CategoryItemStack(createCategoryName(Categories.BUILDING), new ItemStack(Blocks.BRICK_BLOCK)).withKeyBase(Polar.MODID);
		{
		}
		builder.addCategory(building);
		
		gatherAddonGuideContent(builder);
		BOOK_INSTANCE = builder.build();
		return BOOK_INSTANCE;
	}
	
	private void gatherAddonGuideContent(BookBinder builder)
	{
		MinecraftForge.EVENT_BUS.post(new PolarGuideConstructionEvent(builder));
	}

	private static String createCategoryName(String name)
	{
		return CATEGORY_PREFIX + name + ".name";
	}

	private static String createEntryName(String categoryName, String entryName)
	{
		return CATEGORY_PREFIX + categoryName + ".entry." + entryName + ".name";
	}
}
