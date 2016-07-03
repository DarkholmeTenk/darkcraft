package io.darkcraft.mod.client.renderer.gui.system;

import org.lwjgl.opengl.GL11;

import io.darkcraft.mod.client.renderer.LetterRenderer;

public class DaedricLabel extends DarkcraftLabel
{
	public DaedricLabel(int _x, int _y, int width, String _text)
	{
		super(_x, _y, width, _text);
	}

	@Override
	public void render(float pticks, int mouseX, int mouseY)
	{
		if(text == null) return;
		GL11.glPushMatrix();
		GL11.glRotated(180, 1, 0, 0);
		GL11.glTranslatef(0, -12, 0);
		LetterRenderer.render(text, 0, 0, colour, colour, false);
		GL11.glPopMatrix();
	}
}
