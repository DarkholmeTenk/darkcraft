package io.darkcraft.mod.common.magic.blocks;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlockContainer;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.tileent.MagicInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MagicInventoryBlock extends AbstractBlockContainer
{

	public MagicInventoryBlock()
	{
		super(DarkcraftMod.modName);
	}

	@Override
	public void initData()
	{
		setBlockName("MagicInventory");
	}

	@Override
	public void initRecipes()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public TileEntity createNewTileEntity(World w, int m)
	{
		return new MagicInventory(w);
	}

	@Override
	public Class<? extends TileEntity> getTEClass()
	{
		return MagicInventory.class;
	}

}
