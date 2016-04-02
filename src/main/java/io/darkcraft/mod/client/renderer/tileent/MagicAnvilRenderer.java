package io.darkcraft.mod.client.renderer.tileent;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlock;
import io.darkcraft.darkcore.mod.abstracts.AbstractObjRenderer;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.tileent.MagicAnvil;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class MagicAnvilRenderer extends AbstractObjRenderer
{
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
		model.renderAll();
		if(te instanceof MagicAnvil)
		{

		}
	}

}
