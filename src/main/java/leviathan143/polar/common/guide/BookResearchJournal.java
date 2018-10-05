package leviathan143.polar.common.guide;

import amerifrance.guideapi.api.GuideBook;
import amerifrance.guideapi.api.IGuideBook;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.BookBinder;
import amerifrance.guideapi.api.util.PageHelper;
import amerifrance.guideapi.api.util.TextHelper;
import leviathan143.polar.api.guide.*;
import leviathan143.polar.common.Polar;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

@GuideBook
public class BookResearchJournal implements IGuideBook
{
	public static Book BOOK_INSTANCE = null;
	private static final int PAGE_CUTOFF = 260;

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
				.wrap(e -> new EntryAdvancementLocked(e, new ResourceLocation(Polar.MODID, "root")))
				.addPages(PageHelper.pagesForLongText(TextHelper.localizeEffect(Polar.MODID + ".guide.entry.anomalies.part1.text"), PAGE_CUTOFF))
				.addPages(PageHelper.pagesForLongText(TextHelper.localizeEffect(Polar.MODID + ".guide.entry.anomalies.part2.text"), PAGE_CUTOFF))
				.resourceLocation(new ResourceLocation(Polar.MODID, "textures/guide/misc/anomalies_thumb.png"));		
		}
		builder.addCategory(PolarCategories.COMBAT);
		{
			CategoryPopulationHelper populator = new CategoryPopulationHelper(PolarCategories.COMBAT, Polar.MODID);
			populator.addEntry("buff_sword")
				.wrap(e -> new EntryAdvancementLocked(e, new ResourceLocation(Polar.MODID, "blue/root_blue")))
				.addPages(PageHelper.pagesForLongText(TextHelper.localizeEffect(Polar.MODID + ".guide.entry.buff_sword.text"), PAGE_CUTOFF))
				.itemstack(Items.IRON_SWORD);
			populator.addEntry("buff_armour")
				.wrap(e -> new EntryAdvancementLocked(e, new ResourceLocation(Polar.MODID, "red/root_red")))
				.addPages(PageHelper.pagesForLongText(TextHelper.localizeEffect(Polar.MODID + ".guide.entry.buff_armour.text"), PAGE_CUTOFF))
				.itemstack(Items.IRON_CHESTPLATE);
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
