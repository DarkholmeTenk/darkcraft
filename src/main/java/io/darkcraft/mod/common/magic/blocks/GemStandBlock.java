package io.darkcraft.mod.common.magic.blocks;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlockContainer;
import io.darkcraft.darkcore.mod.abstracts.AbstractBlockRenderer;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.renderer.tileent.GemStandRenderer;
import io.darkcraft.mod.common.magic.blocks.tileent.GemStand;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GemStandBlock extends AbstractBlockContainer
{
	public GemStandBlock()
	{
		super(false,DarkcraftMod.modName);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new GemStand();
	}

	@Override
	public Class<? extends TileEntity> getTEClass()
	{
		return GemStand.class;
	}

	@Override
	public void initData()
	{
		setBlockName("GemStand");
		setBlockBounds(0.4f,0,0.4f,0.6f,0.9f,0.6f);
	}

	@Override
	public void initRecipes()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public AbstractBlockRenderer getRenderer()
	{
		return new GemStandRenderer();
	}

	@Override
	public boolean useRendererForItem()
	{
		return true;
	}

}
