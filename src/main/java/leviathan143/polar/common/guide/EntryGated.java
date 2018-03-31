package leviathan143.polar.common.guide;

import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.gui.GuiBase;
import amerifrance.guideapi.gui.GuiCategory;
import leviathan143.polar.api.IPlayerDataPolar;
import leviathan143.polar.api.factions.FactionAlignment;
import leviathan143.polar.api.factions.FactionRank;
import leviathan143.polar.common.capabilities.CapabilityPlayerDataPolar.PlayerDataPolar;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class EntryGated extends EntryAbstract
{
	private final EntryAbstract wrappedEntry;
	private final FactionAlignment requiredAlignment;
	private final FactionRank requiredRank;
	
	public EntryGated(EntryAbstract wrappedEntry, FactionAlignment requiredAlignment, FactionRank requiredRank)
	{
		super(wrappedEntry.name);
		this.wrappedEntry = wrappedEntry;
		this.requiredAlignment = requiredAlignment;
		this.requiredRank = requiredRank;
	}
	
	@Override
	public boolean canSee(EntityPlayer player, ItemStack bookStack)
	{
		IPlayerDataPolar playerData = PlayerDataPolar.get(player);
		boolean correctFaction = playerData.getFaction() == requiredAlignment || playerData.getFaction() == FactionAlignment.UNALIGNED; 
		return playerData.getRank() == requiredRank && correctFaction && wrappedEntry.canSee(player, bookStack);
	}

	@Override
	public void draw(Book book, CategoryAbstract category, int entryX, int entryY, int entryWidth, int entryHeight, int mouseX, int mouseY, GuiBase guiBase, FontRenderer renderer)
	{
		wrappedEntry.draw(book, category, entryX, entryY, entryWidth, entryHeight, mouseX, mouseY, guiBase, renderer);
	}

	@Override
	public void drawExtras(Book book, CategoryAbstract category, int entryX, int entryY, int entryWidth, int entryHeight, int mouseX, int mouseY, GuiBase guiBase, FontRenderer renderer)
	{
		wrappedEntry.drawExtras(book, category, entryX, entryY, entryWidth, entryHeight, mouseX, mouseY, guiBase, renderer);
	}

	@Override
	public void onLeftClicked(Book book, CategoryAbstract category, int mouseX, int mouseY, EntityPlayer player, GuiCategory guiCategory)
	{
		wrappedEntry.onLeftClicked(book, category, mouseX, mouseY, player, guiCategory);
	}

	@Override
	public void onRightClicked(Book book, CategoryAbstract category, int mouseX, int mouseY, EntityPlayer player, GuiCategory guiCategory)
	{
		wrappedEntry.onRightClicked(book, category, mouseX, mouseY, player, guiCategory);
	}

	@Override
	public void onInit(Book book, CategoryAbstract category, GuiCategory guiCategory, EntityPlayer player, ItemStack bookStack)
	{
		wrappedEntry.onInit(book, category, guiCategory, player, bookStack);
	}

	@Override
	public int hashCode()
	{
		return wrappedEntry.hashCode();
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o instanceof EntryGated)
		{
			EntryGated entry = (EntryGated) o;
			return wrappedEntry.equals(entry.wrappedEntry);
		}
		return false;
	}
}
