package io.darkcraft.mod.client.renderer.gui.system;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ChatAllowedCharacters;

import io.darkcraft.darkcore.mod.datastore.Colour;
import io.darkcraft.darkcore.mod.datastore.GuiTexture;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.ITypable;

public class DarkcraftGuiTextfield extends AbstractGuiElement implements ITypable
{
	private boolean inFocus = false;
	public String text = "";
	public int cursorPos = 0;
	public Colour colour = RenderHelper.white;
	public boolean shadow = true;
	private int textWidth;
	private float fontScale = 1;
	private GuiTexture background;

	public DarkcraftGuiTextfield(int x, int y, GuiTexture tex)
	{
		this(x,y,tex.w,tex.h, ((tex.h-4) / (float) RenderHelper.getFontRenderer().FONT_HEIGHT));
		background = tex;
		textWidth = (int) ((tex.w - 4) / fontScale);
	}

	public DarkcraftGuiTextfield(int x, int y, int width)
	{
		this(x, y, width, 1);
	}

	public DarkcraftGuiTextfield(int x, int y, int width, float scale)
	{
		super(x, y, width, (int) ((RenderHelper.getFontRenderer().FONT_HEIGHT * scale) + 2));
		fontScale = scale;
		textWidth = (int) ((width - 2) / scale);
	}

	private DarkcraftGuiTextfield(int x, int y, int w, int h, float scale)
	{
		super(x,y,w,h);
		fontScale = scale;
		textWidth = (int) ((w - 2) / scale);
	}

	@Override
	public void setInFocus(boolean inFocus)
	{
		cursorPos = text.length();
		this.inFocus = inFocus;
	}

	@Override
	public void keyTyped(char c, int i)
	{
		if(i == 203)
			cursorPos = Math.max(0, cursorPos - 1);
		else if(i == 205)
			cursorPos = Math.min(text.length(), cursorPos + 1);
		else if(i == 14)
		{
			if(cursorPos > 0)
			{
				text = remove(cursorPos - 1);
				cursorPos--;
			}
		}
		else if(i == 211)
		{
			if(cursorPos < text.length())
				text = remove(cursorPos);
		}
		else if(isAllowed(c))
		{
			text = insertAtCursor(c);
			cursorPos++;
		}
		triggerChange();
	}

	public boolean isAllowed(char c)
	{
		return ChatAllowedCharacters.isAllowedCharacter(c);
	}

	private String remove(int index)
	{
		String text = this.text;
		int tl = text.length() - 1;
		if((index < 0) || (index > tl)) return text;
		if(index == 0)
		{
			if(tl == 0)
				return "";
			text = text.substring(index + 1);
		}
		else if(index==tl)
			text = text.substring(0, text.length()-1);
		else
			text = text.substring(0, index) + text.substring(index+1);
		return text;
	}

	private String insertAtCursor(char c)
	{
		if(cursorPos <= 0)
			return c + text;
		if(cursorPos >= text.length())
			return text + c;
		return text.substring(0, cursorPos) + c + text.substring(cursorPos);
	}

	private String getModifiedString()
	{
		if(inFocus && ((RenderHelper.getTime() % 2000l) < 1000))
			return insertAtCursor('|');
		return text;
	}

	private String getDisplayString(FontRenderer fr)
	{
		String mstr = getModifiedString();
		return (String) fr.listFormattedStringToWidth(mstr, textWidth).get(0);
	}

	@Override
	public void render(float pticks, int mouseX, int mouseY)
	{
		FontRenderer fr = RenderHelper.getFontRenderer();
		if(background != null)
			background.render(0, 0, 0, true);
		int m = background == null ? 1 : 3;
		GL11.glTranslatef(m, m, 0);
		GL11.glPushMatrix();
		GL11.glScalef(fontScale, fontScale, 1);
		renderText(fr, getDisplayString(fr));
		GL11.glPopMatrix();
	}

	protected void renderText(FontRenderer fr, String t)
	{
		fr.drawString(t, 0, 0, colour.asInt, shadow);
	}

}
