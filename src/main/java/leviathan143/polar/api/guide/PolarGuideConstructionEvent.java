package leviathan143.polar.api.guide;

import amerifrance.guideapi.api.impl.BookBinder;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import leviathan143.polar.common.guide.BookResearchJournal;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Listeners subscribed to this event can be used to add new entries and categories to Polar's guidebook.
 * This event is fired during {@link BookResearchJournal#buildBook()} after Polar adds its categories and
 * entries, but before the book is built. There are constants for Polar's categories in {@link PolarCategories}.
 */
public class PolarGuideConstructionEvent extends Event
{
	private final BookBinder builder;

	public PolarGuideConstructionEvent(BookBinder builder)
	{
		this.builder = builder;
	}
	
	public PolarGuideConstructionEvent addCategory(CategoryAbstract category)
	{
		builder.addCategory(category);
		return this;
	}
}
