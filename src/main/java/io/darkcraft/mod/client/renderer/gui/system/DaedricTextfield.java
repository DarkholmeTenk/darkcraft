package io.darkcraft.mod.client.renderer.gui.system;

import org.lwjgl.opengl.GL11;

import io.darkcraft.darkcore.mod.datastore.GuiTexture;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.client.renderer.LetterRenderer;
import io.darkcraft.mod.common.DaedricLetters;
import net.minecraft.client.gui.FontRenderer;

public class DaedricTextfield extends DarkcraftGuiTextfield
{

	public DaedricTextfield(int _x, int _y, GuiTexture tex)
	{
		super(_x, _y, tex);
	}

	public DaedricTextfield(int _x, int _y, int width)
	{
		super(_x, _y, width);
	}

	public DaedricTextfield(int _x, int _y, int width, float scale)
	{
		super(_x,_y,width,scale);
	}

	@Override
	public boolean isAllowed(char c)
	{
		return DaedricLetters.validChar(c);
	}

	@Override
	protected void renderText(FontRenderer fr, String t)
	{
		GL11.glTranslatef(0, 8, 0);
		GL11.glRotatef(180, 1, 0, 0);
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		LetterRenderer.render(t, 0, 0, colour, RenderHelper.white, false);
	}

}
