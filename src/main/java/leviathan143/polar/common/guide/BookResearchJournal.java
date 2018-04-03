package leviathan143.polar.common.guide;

import amerifrance.guideapi.api.GuideBook;
import amerifrance.guideapi.api.IGuideBook;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.BookBinder;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import leviathan143.polar.api.guide.PolarCategories;
import leviathan143.polar.api.guide.PolarGuideConstructionEvent;
import leviathan143.polar.common.Polar;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

@GuideBook
public class BookResearchJournal implements IGuideBook
{
	public static Book BOOK_INSTANCE = null;

	@Override
	public Book buildBook()
	{
		// Setup the book
		BookBinder builder = new BookBinder(new ResourceLocation(Polar.MODID, "research_journal"));
		builder.setGuideTitle(Polar.MODID + ".guide.title");
		builder.setItemName(Polar.MODID + ".guide.name");
		builder.setHeader("");

		// Add categories, entries and pages
		builder.addCategory(PolarCategories.BASICS);
		{
		}
		builder.addCategory(PolarCategories.COMBAT);
		{
		}
		builder.addCategory(PolarCategories.FARMING);
		{
		}
		builder.addCategory(PolarCategories.BUILDING);
		{
		}
		MinecraftForge.EVENT_BUS.post(new PolarGuideConstructionEvent(builder));
		BOOK_INSTANCE = builder.build();
		return BOOK_INSTANCE;
	}

	private static String createEntryName(CategoryAbstract category, String entryName)
	{
		return PolarCategories.CATEGORY_PREFIX + category.name + ".entry." + entryName + ".name";
	}
}
