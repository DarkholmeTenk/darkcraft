package io.darkcraft.mod.client.renderer.tileent;

import org.lwjgl.opengl.GL11;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlock;
import io.darkcraft.darkcore.mod.abstracts.AbstractObjRenderer;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.blocks.tileent.MagicLight;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class MagicLightRenderer extends AbstractObjRenderer
{

	@Override
	public AbstractBlock getBlock()
	{
		return ItemBlockRegistry.magicLight;
	}

	private IModelCustom ring;
	private ResourceLocation tex = new ResourceLocation(DarkcraftMod.modName, "textures/tileents/light.png");

	private void renderRing(Tessellator tess, long t, int num)
	{
		GL11.glScaled(0.8, 0.8, 0.8);
		double wl = 30;
		double r = ((t + (wl * 0.25 * num)) / wl) % 360.0;

		GL11.glPushMatrix();
		switch(num % 3)
		{
			case 1:GL11.glRotated(25, 1, 0, 0); break;
			case 2:GL11.glRotated(-25, 1, 0, 0); break;
		}
		GL11.glRotated(r, 0, 1, 0);
		ring.renderAll();
		GL11.glPopMatrix();

	}

	private UVStore glowUV = new UVStore(0,0.3125,0.3125,1);
	private void renderGlow(Tessellator tess, long t, int num)
	{
		GL11.glPushMatrix();
		long x = (t + (139713l * num));
		float y = Math.abs((x % 3000) - 1500f) / 1700f;
		float s = 0.3f * (y+0.1f);
		float r = ((x % 100000) * 0.05f);
		GL11.glRotated(r % 360, 0, 1, 0);
		RenderHelper.face(-s, num/200f, -s, s, s, glowUV, true);
		GL11.glPopMatrix();
	}

	@Override
	protected void handleItemRenderType(ItemRenderType type)
	{
		if(type == ItemRenderType.INVENTORY)
			GL11.glScaled(2.65, 2.65, 2.65);
		super.handleItemRenderType(type);
	}

	@Override
	public void renderBlock(Tessellator tess, TileEntity te, int x, int y, int z)
	{
		if(ring == null) ring = AdvancedModelLoader.loadModel(new ResourceLocation(DarkcraftMod.modName,"models/tiles/light.obj"));
		int o = te instanceof MagicLight ? ((MagicLight)te).ct : 0;
		long t = RenderHelper.getTime() + o;
		RenderHelper.bindTexture(tex);
		GL11.glScaled(0.5, 0.5, 0.5);
		GL11.glPushMatrix();
		for(int i = 0; i < 3; i++)
			renderRing(tess, t, i);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
    	GL11.glRotated(RenderManager.instance.playerViewY, 0, -1, 0);
    	GL11.glRotated(RenderManager.instance.playerViewX-90, 1, 0, 0);
		for(int i = 0; i < 5; i++)
			renderGlow(tess, t, i);
		GL11.glPopMatrix();
	}

}
