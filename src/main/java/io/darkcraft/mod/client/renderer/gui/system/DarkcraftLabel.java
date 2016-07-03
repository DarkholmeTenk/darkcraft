package io.darkcraft.mod.client.renderer.gui.system;

import io.darkcraft.darkcore.mod.datastore.Colour;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import net.minecraft.client.gui.FontRenderer;

public class DarkcraftLabel extends AbstractGuiElement
{
	public String text;
	public Colour colour = RenderHelper.white;
	public boolean shadow = true;

	public DarkcraftLabel(int _x, int _y, int width, String _text)
	{
		super(_x, _y, width, RenderHelper.getFontRenderer().FONT_HEIGHT);
		text = _text;
	}

	@Override
	public void render(float pticks, int mouseX, int mouseY)
	{
		if(text == null) return;
		FontRenderer fr = RenderHelper.getFontRenderer();
		fr.drawString(fr.trimStringToWidth(text, w), 0, 0, colour.asInt, shadow);
	}

}
