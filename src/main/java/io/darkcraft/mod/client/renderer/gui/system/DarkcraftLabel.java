package io.darkcraft.mod.client.renderer.gui.system;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;

import io.darkcraft.darkcore.mod.datastore.Colour;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;

public class DarkcraftLabel extends AbstractGuiElement
{
	public String text;
	public Colour colour = RenderHelper.white;
	public boolean shadow = true;

	private final int scale;

	public DarkcraftLabel(int x, int y, String text)
	{
		this(x,y,RenderHelper.getFontRenderer().getStringWidth(text), text);
	}

	public DarkcraftLabel(int x, int y, int width, String text)
	{
		this(x,y,width,text, 1);
	}

	public DarkcraftLabel(int _x, int _y, int width, String _text, int scale)
	{
		super(_x, _y, width, RenderHelper.getFontRenderer().FONT_HEIGHT * scale);
		text = _text;
		this.scale = scale;
	}

	@Override
	public void render(float pticks, int mouseX, int mouseY)
	{
		if(text == null) return;
		FontRenderer fr = RenderHelper.getFontRenderer();
		GL11.glPushMatrix();
		GL11.glScaled(scale, scale, scale);
		fr.drawString(fr.trimStringToWidth(text, w/scale), 0, 0, colour.asInt, shadow);
		GL11.glPopMatrix();
	}

}
