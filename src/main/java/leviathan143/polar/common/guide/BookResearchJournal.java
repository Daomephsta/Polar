package leviathan143.polar.common.guide;

import amerifrance.guideapi.api.GuideBook;
import amerifrance.guideapi.api.IGuideBook;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.Entry;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.category.CategoryItemStack;
import amerifrance.guideapi.page.PageFurnaceRecipe;
import leviathan143.polar.common.Polar;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@GuideBook
public class BookResearchJournal implements IGuideBook
{
	public static final Book BOOK_INSTANCE = new Book();

	private static final String CATEGORY_PREFIX = Polar.MODID + ".guide.category.";

	private static class Categories
	{
		private static final String BASICS_RED = "basics_red";
		private static final String BASICS_BLUE = "basics_blue";
	}

	@Override
	public Book buildBook()
	{
		// Setup the book
		BOOK_INSTANCE.setTitle(Polar.MODID + ".guide.title");
		BOOK_INSTANCE.setDisplayName(Polar.MODID + ".guide.name");
		BOOK_INSTANCE.setWelcomeMessage("");
		BOOK_INSTANCE.setRegistryName(new ResourceLocation(Polar.MODID, "research_journal"));

		// Add categories, entries and pages
		CategoryAbstract redBasics = new CategoryItemStack(createCategoryName(Categories.BASICS_RED),
				new ItemStack(Items.DYE, 1, EnumDyeColor.RED.getDyeDamage())).withKeyBase(Polar.MODID);
		{
			EntryAbstract test = new Entry(createEntryName(Categories.BASICS_RED, "test"));
			test.addPage(new PageFurnaceRecipe(Blocks.COBBLESTONE));
			redBasics.addEntry("test", test);
		}
		BOOK_INSTANCE.addCategory(redBasics);
		CategoryAbstract blueBasics = new CategoryItemStack(createCategoryName(Categories.BASICS_BLUE),
				new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage())).withKeyBase(Polar.MODID);
		{
			EntryAbstract test = new Entry(createEntryName(Categories.BASICS_BLUE, "test"));
			test.addPage(new PageFurnaceRecipe(Blocks.COBBLESTONE));
			blueBasics.addEntry("test", test);
		}
		BOOK_INSTANCE.addCategory(blueBasics);
		return BOOK_INSTANCE;
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
