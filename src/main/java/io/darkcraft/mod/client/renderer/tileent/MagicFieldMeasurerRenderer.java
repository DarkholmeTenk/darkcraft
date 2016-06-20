package io.darkcraft.mod.client.renderer.tileent;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlock;
import io.darkcraft.darkcore.mod.abstracts.AbstractObjRenderer;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.renderer.LetterRenderer;
import io.darkcraft.mod.common.magic.blocks.tileent.MagicFieldMeasurer;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;
import io.darkcraft.mod.common.registries.MagicConfig;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class MagicFieldMeasurerRenderer extends AbstractObjRenderer
{
	private static final ResourceLocation gridTex = new ResourceLocation(DarkcraftMod.modName,"textures/tileents/grid.png");
	@Override
	public AbstractBlock getBlock()
	{
		return ItemBlockRegistry.magicFieldMeasurer;
	}

	private void drawGrids(double[][] vals)
	{
		GL11.glPushMatrix();
		Tessellator tess = Tessellator.instance;
		bindTexture(gridTex);
		double xl = vals.length - 1;
		for(int x = 0; x < (vals.length - 1); x++)
		{
			double zl = vals[x].length - 1;
			tess.startDrawingQuads();
			for(int z = 0; z < (vals[x].length - 1); z++)
			{
				double y = vals[x][z]/MagicConfig.fieldMax;
				double y2 = vals[x+1][z]/MagicConfig.fieldMax;
				double y3 = vals[x][z+1]/MagicConfig.fieldMax;
				double y4 = vals[x+1][z+1]/MagicConfig.fieldMax;

				tess.addVertexWithUV((x+1)/xl, y2, z/zl, 1, 0);
				tess.addVertexWithUV(x/xl, y, z/zl, 0, 0);

				tess.addVertexWithUV(x/xl, y3, (z+1)/zl, 0, 1);
				tess.addVertexWithUV((x+1)/xl, y4, (z+1)/zl, 1, 1);
			}
			tess.draw();
		}
		GL11.glPopMatrix();
	}

	@Override
	public void renderBlock(Tessellator tess, TileEntity te, int x, int y, int z)
	{

	}

	@Override
	public void renderNormal(Tessellator tess, TileEntity te, int x, int y, int z)
	{
		if(!(te instanceof MagicFieldMeasurer))
			return;
		MagicFieldMeasurer mfm = (MagicFieldMeasurer) te;
		double[][] values = mfm.getMagicFieldValues();
		GL11.glTranslated(0, 1.25, 0);
		GL11.glPushMatrix();
		GL11.glScaled(0.05, 0.05, 0.05);
		//LetterRenderer.render('e');
		LetterRenderer.render("abcdefghijklm");
		GL11.glTranslated(0, 12, 0);
		LetterRenderer.render("nopqrstuvwxyz");
		GL11.glPopMatrix();
		//drawGrids(values);
	}

}
