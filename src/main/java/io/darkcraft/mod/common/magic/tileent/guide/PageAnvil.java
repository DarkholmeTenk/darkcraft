package io.darkcraft.mod.common.magic.tileent.guide;

import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.client.renderer.tileent.MagicAnvilRenderer;
import io.darkcraft.mod.client.renderer.tileent.MagicGuideRenderer.GuideData;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class PageAnvil implements IGuidePage
{
	private List<String> lines;
	private List<String> getLines(FontRenderer fr)
	{
		//if(lines == null)
		{
			String main = StatCollector.translateToLocal("dc.guide.anvil");
			main = main.replace("\\n", "\n");
			lines = fr.listFormattedStringToWidth(main, 180);
		}
		return lines;
	}

	@Override
	public void render(GuideData gd)
	{
		GL11.glScaled(0.07, 0.07, 0.07);
		GL11.glTranslated(-5.5, 0, -0);
		MagicAnvilRenderer.i.renderBlock(Tessellator.instance, null, 0, 0, 0);
		FontRenderer fr = RenderHelper.getFontRenderer();
		GL11.glPushMatrix();
		GL11.glRotated(90, 1, 0, 0);
		GL11.glRotated(-90, 0, 0, 1);
		GL11.glRotated(16, 1, 0, 0);
		GL11.glTranslated(2.5, 0, -0.4);
		GL11.glScaled(0.05, 0.05, 0.05);
		int i = 80;
		fr.drawString(ItemBlockRegistry.magicAnvil.getLocalizedName(), -84, i, 0, false);
		List<String> lines = getLines(fr);
		for(String line : lines)
			fr.drawString(line, -140, (i+=10), 0, false);
		GL11.glPopMatrix();
	}
}
