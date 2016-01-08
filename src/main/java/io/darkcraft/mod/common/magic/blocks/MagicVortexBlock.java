package io.darkcraft.mod.common.magic.blocks;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlockContainer;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.tileent.MagicVortex;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MagicVortexBlock extends AbstractBlockContainer
{
	public MagicVortexBlock()
	{
		super(false, DarkcraftMod.modName);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new MagicVortex();
	}

	@Override
	public Class<? extends TileEntity> getTEClass()
	{
		return MagicVortex.class;
	}

	@Override
	public void initData()
	{
		setBlockName("MagicVortex");
		setLightLevel(15f);
	}

	@Override
	public void initRecipes()
	{
	}

}
