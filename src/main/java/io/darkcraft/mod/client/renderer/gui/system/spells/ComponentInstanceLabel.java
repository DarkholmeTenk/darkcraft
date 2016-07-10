package io.darkcraft.mod.client.renderer.gui.system.spells;

import io.darkcraft.darkcore.mod.datastore.Colour;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.client.renderer.gui.system.AbstractGuiElement;
import io.darkcraft.mod.common.magic.systems.spell.ComponentInstance;
import net.minecraft.client.gui.FontRenderer;

public class ComponentInstanceLabel extends AbstractGuiElement
{
	public ComponentInstance compInst;
	public Colour labelColour = Colour.white;
	public boolean shadow = true;
	private String str;

	public ComponentInstanceLabel(int _x, int _y, int width, ComponentInstance ci)
	{
		super(_x, _y, width, 16);
		setCI(ci);
	}

	public void setCI(ComponentInstance ci)
	{
		compInst = ci;
		str = ci.toString();
	}

	@Override
	public void render(float pticks, int mouseX, int mouseY)
	{
		RenderHelper.bindTexture(compInst.component.getIcon());
		RenderHelper.uiFace(0, 0, 16, 16, 0, compInst.component.getIconLocation(), true);
		FontRenderer fr = RenderHelper.getFontRenderer();
		fr.drawString(fr.trimStringToWidth(str, w-20), 18, 4, labelColour.asInt, shadow);
	}

}
