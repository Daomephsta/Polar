package leviathan143.polar.api.guide;

import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.entry.EntryItemStack;
import amerifrance.guideapi.entry.EntryResourceLocation;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
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
		private Function<EntryAbstract, EntryAbstract> wrappingFunction = Function.identity();

		private EntryBuilder(String name)
		{
			this.name = modid + ".guide.entry." + name + ".name";
		}
		
		public EntryBuilder addPages(Collection<IPage> pagesToAdd)
		{
			pages.addAll(pagesToAdd);
			return this;
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
		
		public EntryAbstract itemstack(Block block)
		{
			return itemstack(new ItemStack(block));
		}
		
		public EntryAbstract itemstack(Item item)
		{
			return itemstack(new ItemStack(item));
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
