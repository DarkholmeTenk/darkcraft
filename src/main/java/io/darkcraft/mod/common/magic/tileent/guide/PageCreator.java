package io.darkcraft.mod.common.magic.tileent.guide;

import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.darkcore.mod.multiblock.IMultiBlockStructure;
import io.darkcraft.darkcore.mod.multiblock.RenderBlockAccess;
import io.darkcraft.mod.client.renderer.tileent.MagicGuideRenderer.GuideData;
import io.darkcraft.mod.common.magic.tileent.SpellCreator;

import org.lwjgl.opengl.GL11;

public class PageCreator implements IGuidePage
{
	@Override
	public void render(GuideData gd)
	{
		IMultiBlockStructure struct = SpellCreator.getStructure();
		RenderBlockAccess rba = gd.getRBA(struct);
		GL11.glScaled(0.07, 0.07, 0.07);
		GL11.glTranslated(-2.5, 0, -2.5);
		RenderHelper.renderMultiBlock(struct, rba);
	}

}
