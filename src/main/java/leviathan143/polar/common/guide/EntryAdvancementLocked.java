package leviathan143.polar.common.guide;

import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.gui.GuiBase;
import amerifrance.guideapi.gui.GuiCategory;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class EntryAdvancementLocked extends EntryAbstract
{
	private final EntryAbstract wrappedEntry;
	private final ResourceLocation advancement;

	public EntryAdvancementLocked(EntryAbstract wrappedEntry, ResourceLocation advancement)
	{
		super(wrappedEntry.pageList, wrappedEntry.name, wrappedEntry.unicode);
		this.wrappedEntry = wrappedEntry;
		this.advancement = advancement;
	}

	public boolean canSee(EntityPlayer player, ItemStack bookStack)
	{
		AdvancementList advancements = Minecraft.getMinecraft().player.connection.getAdvancementManager().getAdvancementList();
		boolean hasAdvancement = advancements.getAdvancement(advancement) != null;
		return hasAdvancement && wrappedEntry.canSee(player, bookStack);
	}
	
	public void draw(Book book, CategoryAbstract category, int entryX, int entryY, int entryWidth, int entryHeight, int mouseX, int mouseY, GuiBase guiBase, FontRenderer renderer)
	{
		wrappedEntry.draw(book, category, entryX, entryY, entryWidth, entryHeight, mouseX, mouseY, guiBase, renderer);
	}

	public void drawExtras(Book book, CategoryAbstract category, int entryX, int entryY, int entryWidth, int entryHeight, int mouseX, int mouseY, GuiBase guiBase, FontRenderer renderer)
	{
		wrappedEntry.drawExtras(book, category, entryX, entryY, entryWidth, entryHeight, mouseX, mouseY, guiBase, renderer);
	}

	public void onLeftClicked(Book book, CategoryAbstract category, int mouseX, int mouseY, EntityPlayer player, GuiCategory guiCategory)
	{
		wrappedEntry.onLeftClicked(book, category, mouseX, mouseY, player, guiCategory);
	}

	public void onRightClicked(Book book, CategoryAbstract category, int mouseX, int mouseY, EntityPlayer player, GuiCategory guiCategory)
	{
		wrappedEntry.onRightClicked(book, category, mouseX, mouseY, player, guiCategory);
	}

	public void onInit(Book book, CategoryAbstract category, GuiCategory guiCategory, EntityPlayer player, ItemStack bookStack)
	{
		wrappedEntry.onInit(book, category, guiCategory, player, bookStack);
	}
}
