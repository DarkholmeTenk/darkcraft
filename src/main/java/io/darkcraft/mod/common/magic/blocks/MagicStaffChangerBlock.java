package io.darkcraft.mod.common.magic.blocks;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlockContainer;
import io.darkcraft.darkcore.mod.abstracts.AbstractBlockRenderer;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.renderer.tileent.MagicStaffChangerRenderer;
import io.darkcraft.mod.common.magic.blocks.tileent.MagicStaffChanger;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MagicStaffChangerBlock extends AbstractBlockContainer
{

	public MagicStaffChangerBlock()
	{
		super(false, DarkcraftMod.modName);
		setHardness(1.5F);
	}

	@Override
	public void initData()
	{
		setBlockName("StaffChanger");
	}

	@Override
	public void initRecipes()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new MagicStaffChanger();
	}

	@Override
	public Class<? extends TileEntity> getTEClass()
	{
		return MagicStaffChanger.class;
	}

	@Override
	public AbstractBlockRenderer getRenderer()
	{
		return new MagicStaffChangerRenderer();
	}

}
