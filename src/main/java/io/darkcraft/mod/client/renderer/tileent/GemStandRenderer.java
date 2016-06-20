package io.darkcraft.mod.client.renderer.tileent;

import org.lwjgl.opengl.GL11;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlock;
import io.darkcraft.darkcore.mod.abstracts.AbstractObjRenderer;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.renderer.item.SoulGemRenderer;
import io.darkcraft.mod.common.magic.blocks.tileent.GemStand;
import io.darkcraft.mod.common.magic.items.SoulGem.Size;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class GemStandRenderer extends AbstractObjRenderer
{
	private static IModelCustom model;
	private static ResourceLocation tex = new ResourceLocation(DarkcraftMod.modName,"textures/tileents/gemstand.png");

	@Override
	public AbstractBlock getBlock()
	{
		return ItemBlockRegistry.gemStand;
	}

	@Override
	public void renderBlock(Tessellator tess, TileEntity te, int x, int y, int z)
	{
		if(model == null) model = AdvancedModelLoader.loadModel(new ResourceLocation(DarkcraftMod.modName,"models/tiles/gemstand.obj"));
		RenderHelper.bindTexture(tex);
		model.renderAll();
		if(te instanceof GemStand)
			renderGemStand((GemStand)te);
	}

	private void renderGemStand(GemStand gs)
	{
		Size s = gs.getGemSize();
		if(s != null)
		{
			GL11.glPushMatrix();
			GL11.glTranslatef(0, 1.25f, 0);
			SoulGemRenderer.i.renderSoulGem(s, gs.isGemFull());
			GL11.glPopMatrix();
		}
	}

}
