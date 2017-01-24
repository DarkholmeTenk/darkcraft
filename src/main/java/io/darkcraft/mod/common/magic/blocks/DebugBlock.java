package io.darkcraft.mod.common.magic.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlockContainer;
import io.darkcraft.darkcore.mod.nbt.NBTSerialisable;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.blocks.tileent.DebugTE;

@NBTSerialisable
public class DebugBlock extends AbstractBlockContainer
{

	public DebugBlock()
	{
		super(DarkcraftMod.modName);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new DebugTE();
	}

	@Override
	public Class<? extends TileEntity> getTEClass()
	{
		return DebugTE.class;
	}

	@Override
	public void initData()
	{
		setBlockName("DebugBlock");
	}

	@Override
	public void initRecipes()
	{
		// TODO Auto-generated method stub

	}

}
