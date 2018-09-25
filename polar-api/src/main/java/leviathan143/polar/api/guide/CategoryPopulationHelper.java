package leviathan143.polar.api.guide;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import com.google.common.base.Functions;

import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.entry.EntryItemStack;
import amerifrance.guideapi.entry.EntryResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CategoryPopulationHelper
{
	private final CategoryAbstract category;
	private final String modid;

	public CategoryPopulationHelper(CategoryAbstract category, String modid)
	{
		this.category = category;
		this.modid = modid;
	}

	public EntryBuilder addEntry(String name)
	{
		return new EntryBuilder(name);
	}

	public class EntryBuilder
	{
		final String name;
		private final List<IPage> pages = new ArrayList<>();
		private boolean unicode = false;
		private Function<EntryAbstract, EntryAbstract> wrappingFunction;

		private EntryBuilder(String name)
		{
			this.name = modid + ".guide.entry." + name;
		}
		
		public EntryBuilder addPage(IPage page)
		{
			pages.add(page);
			return this;
		}
		
		public EntryBuilder unicode()
		{
			unicode = true;
			return this;
		}

		public EntryBuilder wrap(UnaryOperator<EntryAbstract> wrappingFunctionIn)
		{
			if (wrappingFunction == null)
				this.wrappingFunction = wrappingFunctionIn;
			else
				this.wrappingFunction = wrappingFunctionIn.compose(this.wrappingFunction);
			return this;
		}
		
		public EntryAbstract itemstack(ItemStack stack)
		{
			return finaliseEntry(new EntryItemStack(pages, name, stack, unicode));
		}
		
		public EntryAbstract resourceLocation(ResourceLocation resourceLocation)
		{
			return finaliseEntry(new EntryResourceLocation(pages, name, resourceLocation, unicode));
		}
		
		private EntryAbstract finaliseEntry(EntryAbstract entry)
		{
			EntryAbstract finalEntry = wrappingFunction.apply(entry);
			CategoryPopulationHelper.this.category.addEntry(new ResourceLocation(modid, finalEntry.name), finalEntry);
			return finalEntry;
		}
	}
}
