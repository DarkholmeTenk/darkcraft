package io.darkcraft.mod.client.renderer.tileent;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.abstracts.AbstractBlock;
import io.darkcraft.darkcore.mod.abstracts.AbstractObjRenderer;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.renderer.LetterRenderer;
import io.darkcraft.mod.common.magic.blocks.tileent.SpellCreator;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

public class SpellCreatorRenderer extends AbstractObjRenderer
{

	@Override
	public AbstractBlock getBlock()
	{
		return ItemBlockRegistry.spellCreatorBlock;
	}

	public void drawFace(SpellCreator sc, int num, float x, float z, float X, float Z, int angle)
	{
		GL11.glPushMatrix();
		UVStore uv = UVStore.defaultUV;
		float h = 0.02f;
		GL11.glTranslated(x+0.2, h, z+1);
		GL11.glScaled(0.1, 0.1, 0.1);
		boolean rot = sc.hasUser[num] != null;
		if(rot)
		{
			GL11.glPushMatrix();
			GL11.glTranslatef(3.5f, 0, -4.5f);
			GL11.glRotated(angle, 0, 1, 0);
			GL11.glTranslatef(-3.5f, 0, 4.5f);
			GL11.glRotated(-90, 1, 0, 0);
			LetterRenderer.render("o");
			GL11.glPopMatrix();
		}
		else
		{
			GL11.glRotated(-90, 1, 0, 0);
			switch(num)
			{
				case 0: LetterRenderer.render("s"); break;
				case 1: LetterRenderer.render("l"); break;
				case 2: LetterRenderer.render("e"); break;
				case 3: LetterRenderer.render("f"); break;
			}
		}
		GL11.glPopMatrix();
	}

	@Override
	public void renderNormal(Tessellator tess, TileEntity te, int x, int y, int z)
	{
		int angle = (int) ((getTime() / 25) % 360);
		SpellCreator sc = (SpellCreator)te;
		if(sc.isValidStructure() && sc.hasSpell())
		{
			GL11.glDisable(GL11.GL_CULL_FACE);
			drawFace(sc, 0,  0,-2,  1,-1, angle);
			drawFace(sc, 1,  0, 2,  1, 3, angle);
			drawFace(sc, 2, -2, 0, -1, 1, angle);
			drawFace(sc, 3,  2, 0,  3, 1, angle);
		}
	}

	private void renderSideText(int angle)
	{
		GL11.glPushMatrix();
		GL11.glRotated(angle, 0, 1, 0);
		GL11.glTranslatef(-18.5f, -15f, 20.1f);
		switch(angle)
		{
			case 0: LetterRenderer.render("spell"); break;
			case 90: LetterRenderer.render("style"); break;
			case 180: LetterRenderer.render(" make"); break;
			case 270: LetterRenderer.render(" break"); break;
		}
		GL11.glPopMatrix();
	}

	private static ResourceLocation tex = new ResourceLocation(DarkcraftMod.modName,"textures/tileents/spellCreator.png");
	private static IModelCustom obj;
	private int xr=1;
	private int yr=2;
	private int zr=0;
	@Override
	public void renderBlock(Tessellator tess, TileEntity te, int x, int y, int z)
	{
		if(obj == null) obj = AdvancedModelLoader.loadModel(new ResourceLocation(DarkcraftMod.modName,"models/tiles/spellcreator.obj"));
		RenderHelper.bindTexture(tex);
		obj.renderPart("Base_Cube.001");

		if(te != null)
		{
			int period = 15;
			if((getTime() % (360 * period*2)) <= period)
			{
				do
				{
					xr = DarkcoreMod.r.nextInt(3);
					yr = DarkcoreMod.r.nextInt(3);
					zr = DarkcoreMod.r.nextInt(3);
				}
				while(((xr==0)&&(yr==0)&&(zr==0)) || ((xr==yr)&&(yr==zr)));
			}
			int angle = (int) ((getTime() / period) % 360);

			SpellCreator sc = (SpellCreator)te;
			if(sc.isValidStructure())
			{
				GL11.glPushMatrix();
				GL11.glTranslatef(0, 2f, 0);
				GL11.glRotatef(angle, xr,yr,zr);
				obj.renderPart("Float_Cube");
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				GL11.glScalef(0.05f, 0.05f, 0.05f);
				renderSideText(0);
				renderSideText(90);
				renderSideText(180);
				renderSideText(270);
				GL11.glPopMatrix();
			}
		}
	}

}
