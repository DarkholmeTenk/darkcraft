package io.darkcraft.mod.common.magic.blocks;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlockContainer;
import io.darkcraft.darkcore.mod.abstracts.AbstractBlockRenderer;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.renderer.tileent.MagicVortexCrystalRenderer;
import io.darkcraft.mod.common.magic.blocks.tileent.MagicVortexCrystal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MagicVortexCrystalBlock extends AbstractBlockContainer
{
	public MagicVortexCrystalBlock()
	{
		super(false, DarkcraftMod.modName);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new MagicVortexCrystal();
	}

	@Override
	public Class<? extends TileEntity> getTEClass()
	{
		return MagicVortexCrystal.class;
	}

	@Override
	public void initData()
	{
		setBlockName("MagicVortexCrystal");
		setLightLevel(5f);
	}

	@Override
	public void initRecipes()
	{
	}

	@Override
	public AbstractBlockRenderer getRenderer()
	{
		return new MagicVortexCrystalRenderer();
	}

	@Override
	public boolean useRendererForItem()
	{
		return true;
	}

}
