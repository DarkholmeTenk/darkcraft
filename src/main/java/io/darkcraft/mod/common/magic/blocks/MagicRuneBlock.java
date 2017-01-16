package io.darkcraft.mod.common.magic.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlockContainer;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.blocks.tileent.MagicRune;

public class MagicRuneBlock extends AbstractBlockContainer
{

	public MagicRuneBlock()
	{
		super(DarkcraftMod.modName);
	}

	@Override
	public void initData()
	{
		setBlockName("MagicRune");
		setHardness(0.1f);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new MagicRune();
	}

	@Override
	public Class<? extends TileEntity> getTEClass()
	{
		return MagicRune.class;
	}

	@Override
	public void initRecipes(){}

}
