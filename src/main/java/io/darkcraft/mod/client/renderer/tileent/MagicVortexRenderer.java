package io.darkcraft.mod.client.renderer.tileent;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlock;
import io.darkcraft.darkcore.mod.abstracts.AbstractBlockRenderer;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.tileent.MagicVortex;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;
import io.darkcraft.mod.common.registries.MagicConfig;

import java.util.WeakHashMap;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class MagicVortexRenderer extends AbstractBlockRenderer
{
	class MagicVortexRenderData
	{
		public int size;
		public float[] ringOffsets;
		public float[] radSizes;
		public float[] heights;
		public int[] textures;
		public int[] speeds;

		MagicVortexRenderData(int s)
		{
			generateRingOffsets(s);
			size = s;
		}

		public void generateRingOffsets(int r)
		{
			if((ringOffsets != null) && (ringOffsets.length >= r)) return;
			size = r;
			ringOffsets = new float[r];
			textures = new int[r];
			heights = new float[r];
			radSizes = new float[r];
			speeds = new int[r];
			for(int i = 0; i < r; i++)
			{
				ringOffsets[i] = DarkcraftMod.modRand.nextFloat() * 360;
				textures[i] = DarkcraftMod.modRand.nextInt(16);
				heights[i] = 1+Math.abs((i * 0.6f) + DarkcraftMod.modRand.nextFloat());
				radSizes[i] = 0.5f + (heights[i] * (0.6f + (((float)DarkcraftMod.modRand.nextGaussian()) * 0.1f)));
				speeds[i] = (int) (1500 * ((1 +DarkcraftMod.modRand.nextFloat()) * (1 + (i * 0.25))));
				if(DarkcraftMod.modRand.nextDouble() < 0.3)
					speeds[i] = -speeds[i];
			}
		}

		private float timeAngle(int rNum)
		{
			int rotTime = speeds[rNum];
			if(rotTime < 0)
			{
				rotTime = -rotTime;
				return (1 - ((getTime() % rotTime) / (float)rotTime)) * 360;
			}
			return ((getTime() % rotTime) / (float)rotTime) * 360;
		}

		public float getAngle(int rNum)
		{
			if(rNum >= size)
				generateRingOffsets(rNum);
			float offset = ringOffsets[rNum];
			return (offset + timeAngle(rNum)) % 360;
		}

		public float getHeight(int rNum)
		{
			if(rNum >= size)
				generateRingOffsets(rNum);
			return heights[rNum];
		}

		public float getRadius(int rNum)
		{
			if(rNum >= size)
				generateRingOffsets(rNum);
			return radSizes[rNum];
		}

		public float getTexturev(int rNum)
		{
			if(rNum >= size)
				generateRingOffsets(rNum);
			return textures[rNum]/16f;
		}
		public float getTextureV(int rNum)
		{
			if(rNum >= size)
				generateRingOffsets(rNum);
			return (textures[rNum]+1)/16f;
		}
	}

	private static ResourceLocation tex = new ResourceLocation(DarkcraftMod.modName,"textures/tileents/flow.png");
	private static WeakHashMap<TileEntity,MagicVortexRenderData> mvrdMap = new WeakHashMap();

	@Override
	public AbstractBlock getBlock()
	{
		return ItemBlockRegistry.magicVortex;
	}

	private MagicVortexRenderData getMVRD(TileEntity te)
	{
		if(mvrdMap.containsKey(te))
			return mvrdMap.get(te);
		else
		{
			MagicVortexRenderData mvrd = new MagicVortexRenderData(MagicConfig.magicVortexNumRings);
			mvrdMap.put(te,mvrd);
			return mvrd;
		}
	}

	private void renderRing(MagicVortex mv, MagicVortexRenderData mvrd, int num, Tessellator tess)
	{
		GL11.glPushMatrix();
		double h = 0.125;
		float angle = mvrd.getAngle(num);
		float w = mvrd.getRadius(num);
		float yOff = mvrd.getHeight(num);
		GL11.glTranslated(0.5, yOff, 0.5);
		GL11.glScaled(w, w, w);
		GL11.glRotated(angle, 0, 1, 0);
		float v = mvrd.getTexturev(num);
		float V = mvrd.getTextureV(num);
		int numSegments = 10;
		float aps = 6;
		tess.startDrawingQuads();
		for(int i = 0; i < numSegments; i++)
		{
			float tMin = (i / (float)(numSegments));
			float tMax = ((i+1) / (float)(numSegments));
			/*double ang = Math.toRadians(i * aps);
			double ang2 = Math.toRadians((i+1)*aps);
			double x = Math.sin(ang);
			double X = Math.sin(ang2);
			double z = Math.cos(ang);
			double Z = Math.cos(ang2);*/
			double ang = (i * aps);
			double ang2 = ((i+1)*aps);
			double x = MathHelper.sin(ang);
			double X = MathHelper.sin(ang2);
			double z = MathHelper.cos(ang);
			double Z = MathHelper.cos(ang2);
			tess.addVertexWithUV(x, 0, z, tMin, V);
			tess.addVertexWithUV(X, 0, Z, tMax, V);
			tess.addVertexWithUV(X, h, Z, tMax, v);
			tess.addVertexWithUV(x, h, z, tMin, v);
		}
		tess.draw();
		GL11.glPopMatrix();
	}

	@Override
	public void renderBlock(Tessellator tess, TileEntity te, int x, int y, int z)
	{
		if(!(te instanceof MagicVortex)) return;
		MagicVortex mv = (MagicVortex)te;
		MagicVortexRenderData mvrd = getMVRD(te);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glColor3f(1f, 0.24f, 0.1f);
		bindTexture(tex);
		for(int i = 0; i < mvrd.size; i++)
			renderRing(mv, mvrd, i, tess);
		GL11.glColor3f(1, 1, 1);
		GL11.glEnable(GL11.GL_CULL_FACE);
	}

}