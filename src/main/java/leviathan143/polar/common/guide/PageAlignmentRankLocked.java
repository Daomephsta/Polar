package leviathan143.polar.common.guide;

import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.Page;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.gui.GuiBase;
import amerifrance.guideapi.gui.GuiEntry;
import leviathan143.polar.api.factions.FactionAlignment;
import leviathan143.polar.api.factions.FactionRank;
import leviathan143.polar.common.capabilities.CapabilityPlayerDataPolar.PlayerDataPolar;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class PageAlignmentRankLocked extends Page
{
	private final IPage wrappedPage;
	private final FactionAlignment requiredAlignment;
	private final FactionRank requiredRank;
	
	public PageAlignmentRankLocked(IPage wrappedPage, FactionAlignment requiredAlignment, FactionRank requiredRank)
	{
		this.wrappedPage = wrappedPage;
		this.requiredAlignment = requiredAlignment;
		this.requiredRank = requiredRank;
	}
	
	@Override
	public boolean canSee(Book book, CategoryAbstract category, EntryAbstract entry, EntityPlayer player, ItemStack bookStack, GuiEntry guiEntry)
	{
		PlayerDataPolar playerData = PlayerDataPolar.get(player);
		return playerData.getRank() == requiredRank && playerData.getFaction() == requiredAlignment
				&& wrappedPage.canSee(book, category, entry, player, bookStack, guiEntry);
	}

	@Override
	public void draw(Book book, CategoryAbstract category, EntryAbstract entry, int guiLeft, int guiTop, int mouseX, int mouseY, GuiBase guiBase, FontRenderer fontRendererObj)
	{
		wrappedPage.draw(book, category, entry, guiLeft, guiTop, mouseX, mouseY, guiBase, fontRendererObj);
	}

	@Override
	public void drawExtras(Book book, CategoryAbstract category, EntryAbstract entry, int guiLeft, int guiTop, int mouseX, int mouseY, GuiBase guiBase, FontRenderer fontRendererObj)
	{
		wrappedPage.drawExtras(book, category, entry, guiLeft, guiTop, mouseX, mouseY, guiBase, fontRendererObj);
	}

	@Override
	public void onLeftClicked(Book book, CategoryAbstract category, EntryAbstract entry, int mouseX, int mouseY, EntityPlayer player, GuiEntry guiEntry)
	{
		wrappedPage.onLeftClicked(book, category, entry, mouseX, mouseY, player, guiEntry);
	}

	@Override
	public void onRightClicked(Book book, CategoryAbstract category, EntryAbstract entry, int mouseX, int mouseY, EntityPlayer player, GuiEntry guiEntry)
	{
		wrappedPage.onRightClicked(book, category, entry, mouseX, mouseY, player, guiEntry);
	}

	@Override
	public void onInit(Book book, CategoryAbstract category, EntryAbstract entry, EntityPlayer player, ItemStack bookStack, GuiEntry guiEntry)
	{
		wrappedPage.onInit(book, category, entry, player, bookStack, guiEntry);
	}
	
	@Override
	public boolean equals(Object o)
	{
		return wrappedPage.equals(o);
	}
}
