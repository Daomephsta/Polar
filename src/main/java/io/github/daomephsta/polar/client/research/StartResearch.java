package io.github.daomephsta.polar.client.research;

import org.w3c.dom.Element;

import io.github.daomephsta.inscribe.client.guide.gui.GuideGui;
import io.github.daomephsta.inscribe.client.guide.gui.InteractableElement;
import io.github.daomephsta.inscribe.client.guide.xmlformat.InscribeSyntaxException;
import io.github.daomephsta.inscribe.client.guide.xmlformat.XmlAttributes;
import io.github.daomephsta.polar.common.components.PolarPlayerDataComponent.PolarPlayerData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class StartResearch implements InteractableElement
{
    private final Identifier research;

    public StartResearch(Element args) throws InscribeSyntaxException
    {
        this.research = XmlAttributes.asIdentifier(args, "research");
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        MinecraftClient client = MinecraftClient.getInstance();
        PolarPlayerData.get(client.player).startResearch(research);
        ((GuideGui) client.currentScreen).reopen();
        return true;
    }
}
