package leviathan143.polar.common.guide;

import java.lang.reflect.Field;
import java.util.Map;

import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.gui.GuiBase;
import amerifrance.guideapi.gui.GuiCategory;
import daomephsta.umbra.reflection.MappingAgnosticReflectionHelper;
import net.minecraft.advancements.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class EntryAdvancementLocked extends EntryAbstract
{
	private static final Field fAdvancementToProgress = MappingAgnosticReflectionHelper.findField(ClientAdvancementManager.class, "field_192803_d");
	private final EntryAbstract wrappedEntry;
	private final ResourceLocation advancementID;

	public EntryAdvancementLocked(EntryAbstract wrappedEntry, ResourceLocation advancementID)
	{
		super(wrappedEntry.pageList, wrappedEntry.name, wrappedEntry.unicode);
		this.wrappedEntry = wrappedEntry;
		this.advancementID = advancementID;
	}

	@Override
	public boolean canSee(EntityPlayer player, ItemStack bookStack)
	{
		return hasAdvancement() && wrappedEntry.canSee(player, bookStack);
	}
	
	@SuppressWarnings("unchecked")
	private boolean hasAdvancement()
	{
		try
		{
			ClientAdvancementManager advancementManager = Minecraft.getMinecraft().player.connection.getAdvancementManager();
			AdvancementList advancements = advancementManager.getAdvancementList();
			Advancement advancement = advancements.getAdvancement(advancementID);
			return advancement != null 
				&& ((Map<Advancement, AdvancementProgress>) fAdvancementToProgress.get(advancementManager)).get(advancement).isDone();
		}
		catch (IllegalArgumentException | IllegalAccessException e)
		{
			throw new RuntimeException("Failed to reflectively access ClientAdvancementManager#field_192803_d", e);
		}
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
}
