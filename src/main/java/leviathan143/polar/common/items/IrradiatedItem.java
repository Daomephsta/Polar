package leviathan143.polar.common.items;

import leviathan143.polar.api.Polarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class IrradiatedItem extends Item
{
	private final ItemStack precursor;
	private final Polarity polarity;
	
	public IrradiatedItem(Item precursor, Polarity polarity)
	{
		this(new ItemStack(precursor), polarity);
	}
	
	public IrradiatedItem(ItemStack precursor, Polarity polarity)
	{
		this.precursor = precursor;
		this.polarity = polarity;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		String precursorName = net.minecraft.util.text.translation.I18n.translateToLocal(precursor.getTranslationKey() + ".name").trim();
		return net.minecraft.util.text.translation.I18n.translateToLocalFormatted(polarity.getTranslationKey() + ".irradiated", precursorName);
	}
}
