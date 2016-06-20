package io.darkcraft.mod.client.renderer.tileent;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlock;
import io.darkcraft.darkcore.mod.abstracts.AbstractObjRenderer;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.blocks.tileent.MagicAnvil;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

public class MagicAnvilRenderer extends AbstractObjRenderer
{
	public static final MagicAnvilRenderer i = new MagicAnvilRenderer();
	private static final ResourceLocation tex = new ResourceLocation(DarkcraftMod.modName, "textures/tileents/anvil.png");
	private static IModelCustom model;

	@Override
	public AbstractBlock getBlock()
	{
		return ItemBlockRegistry.magicAnvil;
	}

	@Override
	public void renderBlock(Tessellator tess, TileEntity te, int x, int y, int z)
	{
		if(model == null) model = AdvancedModelLoader.loadModel(new ResourceLocation(DarkcraftMod.modName, "models/tiles/anvil.obj"));
		bindTexture(tex);
		boolean ang = false;
		if(te != null)
			ang = ((te.getBlockMetadata() & 1) == 0);
		if(ang)
			GL11.glRotated(90, 0, 1, 0);
		model.renderAll();
		if(te instanceof MagicAnvil)
		{
			MagicAnvil ma = (MagicAnvil)te;
			if(ma.eiArr == null) return;
			double d = 0.75;
			for(int i = 0; i < 3; i++)
				if(ma.eiArr[i] != null)
					ma.eiArr[i].hoverStart = 0;
			if(ma.eiArr[0] != null)
				RenderManager.instance.renderEntityWithPosYaw(ma.eiArr[0], 0,1.5,-d, 0,ma.eiArr[0].rotationYaw);
			if(ma.eiArr[1] != null)
				RenderManager.instance.renderEntityWithPosYaw(ma.eiArr[1], 0,1.5,0, 0,ma.eiArr[1].rotationYaw);
			if(ma.eiArr[2] != null)
				RenderManager.instance.renderEntityWithPosYaw(ma.eiArr[2], 0,1.5,d, 0,ma.eiArr[2].rotationYaw);
		}
	}
}
