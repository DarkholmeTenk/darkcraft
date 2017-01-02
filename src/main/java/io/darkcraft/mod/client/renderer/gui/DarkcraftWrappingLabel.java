package io.darkcraft.mod.client.renderer.gui;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;

import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftLabel;

public class DarkcraftWrappingLabel extends DarkcraftLabel
{
	private FontRenderer fontRenderer = RenderHelper.getFontRenderer();
	private int sizePerLine = fontRenderer.FONT_HEIGHT;

	private List<String> textList;

	public DarkcraftWrappingLabel(int _x, int _y, int width, String text)
	{
		super(_x, _y, width, text);
		setText(text);
	}

	public void setText(String text)
	{
		text = text.replace("\\n", "\n");
		setText(fontRenderer.listFormattedStringToWidth(text, w));
	}

	private void setText(List<String> text)
	{
		textList = text;
		setSize(w, (sizePerLine * textList.size()) + 1);
	}

	@Override
	public void render(float pticks, int mouseX, int mouseY)
	{
		if((textList == null) || textList.isEmpty()) return;
		GL11.glTranslated(0, 1, 0);
		int cy = 0 - sizePerLine;
		for(String s : textList)
			fontRenderer.drawString(s, 0, cy += sizePerLine, colour.asInt, shadow);
	}

}
