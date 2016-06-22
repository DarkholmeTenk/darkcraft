package io.darkcraft.mod.common.magic.blocks;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlockContainer;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.renderer.tileent.MagicSymbolRenderer;
import io.darkcraft.mod.common.magic.blocks.tileent.MagicSymbol;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MagicSymbolBlock extends AbstractBlockContainer
{
	public MagicSymbolBlock()
	{
		super(false, DarkcraftMod.modName);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new MagicSymbol();
	}

	@Override
	public Class<? extends TileEntity> getTEClass()
	{
		return MagicSymbol.class;
	}

	@Override
	public void initData()
	{
		setBlockName("MagicSymbol");
		setHardness(0);
		setBlockBounds(0, 0, 0, 1, 0.05f, 1);
	}

	@Override
	public void initRecipes(){}

	@Override
	public boolean canHarvestBlock(EntityPlayer player, int meta)
    {
		return false;
    }

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	public boolean isNormalBlock()
	{
		return false;
	}

	@Override
	public Object getRenderer()
	{
		return new MagicSymbolRenderer();
	}

}
