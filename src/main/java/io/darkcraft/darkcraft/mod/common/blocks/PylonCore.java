package io.darkcraft.darkcraft.mod.common.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import io.darkcraft.darkcore.mod.abstracts.AbstractBlockContainer;
import io.darkcraft.darkcraft.mod.common.tileent.PylonCoreTE;

public class PylonCore extends AbstractBlockContainer
{
	public PylonCore()
	{
		super("darkcraft");
	}
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new PylonCoreTE();
	}

	@Override
	public void initData()
	{
		setBlockName("PylonCore");
	}

	@Override
	public void initRecipes()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Class<? extends TileEntity> getTEClass()
	{
		return PylonCoreTE.class;
	}

}
