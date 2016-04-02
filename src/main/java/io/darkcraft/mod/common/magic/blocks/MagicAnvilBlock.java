package io.darkcraft.mod.common.magic.blocks;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlockContainer;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.tileent.MagicAnvil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MagicAnvilBlock extends AbstractBlockContainer
{

	public MagicAnvilBlock()
	{
		super(false, DarkcraftMod.modName);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new MagicAnvil();
	}

	@Override
	public Class<? extends TileEntity> getTEClass()
	{
		return MagicAnvil.class;
	}

	@Override
	public void initData()
	{
		setBlockName("MagicAnvil");
	}

	@Override
	public void initRecipes()
	{
		// TODO Auto-generated method stub

	}

}
