package io.darkcraft.mod.client.renderer.tileent;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.abstracts.AbstractBlock;
import io.darkcraft.darkcore.mod.abstracts.AbstractObjRenderer;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.blocks.tileent.MagicVortexCrystal;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;

import java.util.WeakHashMap;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

public class MagicVortexCrystalRenderer extends AbstractObjRenderer
{
	private static IModelCustom model;
	private static ResourceLocation tex = new ResourceLocation(DarkcraftMod.modName,"textures/tileents/crystal.png");

	private static WeakHashMap<TileEntity,Float> floatMap = new WeakHashMap<TileEntity,Float>();

	@Override
	public AbstractBlock getBlock()
	{
		return ItemBlockRegistry.magicVortexCrystal;
	}

	private float getR()
	{
		int rotTime = 7000;
		long t = getTime();
		float p = (t % rotTime) / (float)rotTime;
		return (p * 360);
	}

	private float getAngle(float r,TileEntity te)
	{
		float f;
		if(floatMap.containsKey(te))
			f = floatMap.get(te);
		else
			floatMap.put(te, f=r);
		return f;
	}

	public float getRot(TileEntity te)
	{
		float r = getR();
		float f = getAngle(DarkcoreMod.r.nextFloat()*360,te);
		return (r + f) % 360f;
	}

	@Override
	public void renderBlock(Tessellator tess, TileEntity te, int x, int y, int z)
	{
		if(!(te instanceof MagicVortexCrystal)) return;
		MagicVortexCrystal mvc = (MagicVortexCrystal) te;
		{
			GL11.glPushMatrix();
			GL11.glScaled(0.75, 0.75, 0.75);
			RenderManager.instance.renderEntityWithPosYaw(mvc.getISEntity(), 0.125, -0.25, 0, 90, 90);
			GL11.glPopMatrix();
		}
		{
			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			if(model == null) model = AdvancedModelLoader.loadModel(new ResourceLocation(DarkcraftMod.modName,"models/tiles/crystal.obj"));
			bindTexture(tex);
			float f = getRot(te);
			GL11.glColor3d(mvc.cols[0], mvc.cols[1], mvc.cols[2]);
			GL11.glRotatef(getAngle(0,te) % 360,0,1,0);
			GL11.glRotatef(f,1,0,0);
			model.renderAll();
			GL11.glPopMatrix();
		}
	}

	@Override
	public void renderNormal(Tessellator tess, TileEntity te, int x, int y, int z)
	{

	}

}
