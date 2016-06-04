package io.darkcraft.mod.common.tech.block;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlockContainer;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.tech.tileent.TechBattery;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TechBatteryBlock extends AbstractBlockContainer
{
	public TechBatteryBlock()
	{
		super(DarkcraftMod.modName);
	}

	@Override
	public void initData()
	{
		setBlockName("Battery");
	}

	@Override
	public void initRecipes()
	{
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new TechBattery();
	}

	@Override
	public Class<? extends TileEntity> getTEClass()
	{
		return TechBattery.class;
	}

}
