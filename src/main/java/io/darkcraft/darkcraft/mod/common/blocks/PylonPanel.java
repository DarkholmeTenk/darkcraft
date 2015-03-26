package io.darkcraft.darkcraft.mod.common.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import io.darkcraft.darkcore.mod.abstracts.AbstractBlockContainer;
import io.darkcraft.darkcraft.mod.common.tileent.PylonPanelTE;

public class PylonPanel extends AbstractBlockContainer
{
	public PylonPanel()
	{
		super("darkcraft");
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new PylonPanelTE();
	}

	@Override
	public Class<? extends TileEntity> getTEClass()
	{
		return PylonPanelTE.class;
	}

	@Override
	public void initData()
	{
		setBlockName("PylonPanel");
	}

	@Override
	public void initRecipes()
	{
		// TODO Auto-generated method stub

	}

}
