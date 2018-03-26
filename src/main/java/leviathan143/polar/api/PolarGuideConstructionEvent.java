package leviathan143.polar.api;

import amerifrance.guideapi.api.impl.BookBinder;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PolarGuideConstructionEvent extends Event
{
	private final BookBinder builder;

	public PolarGuideConstructionEvent(BookBinder builder)
	{
		this.builder = builder;
	}
	
	public void addCategory(CategoryAbstract category) 
	{
		builder.addCategory(category);
	}
}
