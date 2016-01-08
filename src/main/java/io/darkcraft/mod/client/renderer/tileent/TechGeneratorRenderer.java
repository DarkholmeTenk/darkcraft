package io.darkcraft.mod.client.renderer.tileent;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlock;
import io.darkcraft.darkcore.mod.abstracts.AbstractObjRenderer;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;
import io.darkcraft.mod.common.tech.tileent.TechGenerator;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

public class TechGeneratorRenderer extends AbstractObjRenderer
{
	private IModelCustom model;
	private ResourceLocation tex = new ResourceLocation(DarkcraftMod.modName, "textures/tileents/generator.png");
	private ResourceLocation coreTex = new ResourceLocation(DarkcraftMod.modName, "textures/tileents/vertflow.png");

	{
		model = AdvancedModelLoader.loadModel(new ResourceLocation(DarkcraftMod.modName, "models/tiles/generator.obj"));
	}

	@Override
	public AbstractBlock getBlock()
	{
		return ItemBlockRegistry.techGenerator;
	}

	@Override
	public void renderBlock(Tessellator tess, TileEntity te, int x, int y, int z)
	{
		bindTexture(tex);
		TechGenerator gen = (TechGenerator) te;
		model.renderPart("Generator");
		GL11.glPushMatrix();
		int mul = 15;
		double div = 20;
		double hMul = 0.8;
		int tt = gen.tt;
		for(int i = 0; i < 4; i++)
		{
			GL11.glRotated(90, 0, 1, 0);
			double h = hMul * Math.sin((tt += mul) / div);
			GL11.glPushMatrix();
			GL11.glTranslated(0, h, 0);
			model.renderPart("Pylon1");
			GL11.glPopMatrix();
			h = hMul * Math.sin((tt += mul) / div);
			GL11.glPushMatrix();
			GL11.glTranslated(0, h, 0);
			model.renderPart("Pylon2");
			GL11.glPopMatrix();
		}
		GL11.glPopMatrix();
	}

	private void renderSquare(Tessellator tess, double bW, double tW, double bY, double tY)
	{
		double hm = 0.5;
		double hM = 2;
		double h = hM - hm;
		double c = 0;

		GL11.glPushMatrix();
		for(int i = 0; i < 4; i++)
		{
			GL11.glRotated(90, 0, 1, 0);
			tess.startDrawingQuads();
			tess.addVertexWithUV(c-bW, hm+(h*bY), c+bW, 0, bY);
			tess.addVertexWithUV(c+bW, hm+(h*bY), c+bW, 1, bY);
			tess.addVertexWithUV(c+tW, hm+(h*tY), c+tW, 1, tY);
			tess.addVertexWithUV(c-tW, hm+(h*tY), c+tW, 0, tY);
			tess.draw();
		}
		GL11.glPopMatrix();
	}

	@Override
	public void renderNormal(Tessellator tess, TileEntity te, int x, int y, int z)
	{
		TechGenerator gen = (TechGenerator) te;
		bindTexture(coreTex);
		GL11.glTranslated(0.5, 0, 0.5);
		int tn = 40;
		double tb = 0.05;
		double bh = (((gen.tt % tn) / (double)tn) * (1 + (2 * tb))) - tb;
		double bH = 0.1;
		double bW = 0.3;
		double sW = 0.19;
		renderSquare(tess, sW, sW, 0, bh-bH);
		renderSquare(tess, sW, bW, bh-bH,bh);
		renderSquare(tess, bW, sW, bh,bh+bH);
		renderSquare(tess, sW, sW, bh+bH,1);
	}

}
