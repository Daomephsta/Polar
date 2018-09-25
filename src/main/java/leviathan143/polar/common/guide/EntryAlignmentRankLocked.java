package leviathan143.polar.common.guide;

import com.google.common.base.Objects;

import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.gui.GuiBase;
import amerifrance.guideapi.gui.GuiCategory;
import leviathan143.polar.api.capabilities.IPlayerDataPolar;
import leviathan143.polar.api.factions.FactionAlignment;
import leviathan143.polar.api.factions.FactionRank;
import leviathan143.polar.common.capabilities.CapabilityPlayerDataPolar.PlayerDataPolar;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class EntryAlignmentRankLocked extends EntryAbstract
{
	private final EntryAbstract wrappedEntry;
	private final FactionAlignment requiredAlignment;
	private final FactionRank requiredRank;

	public EntryAlignmentRankLocked(EntryAbstract wrappedEntry, FactionAlignment requiredAlignment, FactionRank requiredRank)
	{
		super(wrappedEntry.pageList, wrappedEntry.name, wrappedEntry.unicode);
		this.wrappedEntry = wrappedEntry;
		this.requiredAlignment = requiredAlignment;
		this.requiredRank = requiredRank;
	}

	@Override
	public boolean canSee(EntityPlayer player, ItemStack bookStack)
	{
		IPlayerDataPolar playerData = PlayerDataPolar.get(player);
		boolean correctFaction = playerData.getFaction() == requiredAlignment || requiredAlignment == FactionAlignment.UNALIGNED;
		boolean correctRank = FactionRank.COMPARATOR.compare(playerData.getRank(), requiredRank) >= 0;
		return correctRank && correctFaction && wrappedEntry.canSee(player, bookStack);
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
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((requiredAlignment == null) ? 0 : requiredAlignment.hashCode());
		result = prime * result + ((requiredRank == null) ? 0 : requiredRank.hashCode());
		result = prime * result + ((wrappedEntry == null) ? 0 : wrappedEntry.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj instanceof EntryAlignmentRankLocked)
		{
			EntryAlignmentRankLocked other = (EntryAlignmentRankLocked) obj;
			if (requiredAlignment != other.requiredAlignment)
				return false;
			if (requiredRank != other.requiredRank)
				return false;
			if (!Objects.equal(wrappedEntry, other.wrappedEntry))
				return false;
			return true;
		}
		return false;
	}
}
