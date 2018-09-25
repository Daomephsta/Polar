package leviathan143.polar.common.guide;

import amerifrance.guideapi.api.GuideBook;
import amerifrance.guideapi.api.IGuideBook;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.BookBinder;
import amerifrance.guideapi.page.PageText;
import leviathan143.polar.api.guide.*;
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
		builder.setCreativeTab(Polar.TAB_OTHER);

		// Add categories, entries and pages
		builder.addCategory(PolarCategories.BASICS);
		{
			CategoryPopulationHelper populator = new CategoryPopulationHelper(PolarCategories.BASICS, Polar.MODID);
			populator.addEntry("anomalies")
				.wrap(e -> new EntryAdvancementLocked(e, new ResourceLocation(Polar.MODID, "polar/root")))
				.addPage(new PageText(Polar.MODID + ".guide.anomalies.page1.text"))
				.resourceLocation(new ResourceLocation(Polar.MODID, "textures/guide/misc/anomaly_thumb.png"));		
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
}
