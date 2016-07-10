package io.darkcraft.mod.client.renderer.gui.system.daedric;

import org.lwjgl.opengl.GL11;

import io.darkcraft.darkcore.mod.datastore.GuiTexture;
import io.darkcraft.mod.client.renderer.LetterRenderer;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftGuiButton;
import io.darkcraft.mod.common.DaedricLetters;

public class DaedricButton extends DarkcraftGuiButton
{
	public DaedricButton(String _id, int x, int y, String str)
	{
		super(_id, x,y,LetterRenderer.width(str), DaedricLetters.maxHeight, str);
	}

	public DaedricButton(String _id, int x, int y, int w, String str)
	{
		super(_id, x,y,w, DaedricLetters.maxHeight, str);
	}

	public DaedricButton(String _id, int x, int y, GuiTexture tex, String str)
	{
		super(_id, x, y, tex, str);
	}

	@Override
	public void render(float pticks, int mouseX, int mouseY)
	{
		if(!visible) return;
		if(buttonTex != null)
		{
			if(enabled && (hoverTex != null) && withinBounds(mouseX,mouseY))
				hoverTex.render(0, 0, 0, true);
			else
				buttonTex.render(0, 0, 0, true);
		}
		if((strTex == null) || strTex.isEmpty()) return;
		GL11.glPushMatrix();
		GL11.glRotated(180, 1, 0, 0);
		GL11.glTranslatef(0, -h, 0);
		LetterRenderer.render(strTex, 0, 0, strColour, strColour, false);
		GL11.glPopMatrix();
	}
}
