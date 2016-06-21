package io.darkcraft.mod.client.renderer.gui.system;

import io.darkcraft.darkcore.mod.datastore.Colour;
import io.darkcraft.darkcore.mod.datastore.GuiTexture;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IClickable;
import net.minecraft.client.gui.FontRenderer;

public class DarkcraftGuiButton extends AbstractGuiElement implements IClickable
{
	private final String id;
	private final GuiTexture buttonTex;
	public GuiTexture hoverTex;
	public String strTex;
	public Colour strColour = RenderHelper.white;
	public boolean strShadow = true;

	public DarkcraftGuiButton(String _id, int x, int y, GuiTexture tex, String str)
	{
		super(x,y,tex.w, tex.h);
		id = _id;
		buttonTex = tex;
		strTex = str;
	}

	public boolean mousePressed(int button, int x, int y)
	{
		if(!enabled || !visible) return false;
		return false;
	}

	@Override
	public boolean click(int button, int x, int y)
	{
		if(parent != null)
			parent.clickableClicked(this, id);
		return true;
	}

	@Override
	public void render(float pticks, int mouseX, int mouseY)
	{
		if(!visible) return;
		if(enabled && (hoverTex != null) && withinBounds(mouseX,mouseY))
			hoverTex.render(0, 0, 0, true);
		else
			buttonTex.render(0, 0, 0, true);
		if((strTex == null) || strTex.isEmpty()) return;
		FontRenderer fr = RenderHelper.getFontRenderer();
		int sw = fr.getStringWidth(strTex);
		fr.drawString(strTex, (w-sw)/2, (h-fr.FONT_HEIGHT)/2, strColour.asInt, strShadow);
	}
}
