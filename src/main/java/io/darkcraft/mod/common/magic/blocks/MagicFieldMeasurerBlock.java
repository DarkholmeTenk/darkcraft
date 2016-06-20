package io.darkcraft.mod.common.magic.blocks;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlockContainer;
import io.darkcraft.darkcore.mod.abstracts.AbstractBlockRenderer;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.renderer.tileent.MagicFieldMeasurerRenderer;
import io.darkcraft.mod.common.magic.blocks.tileent.MagicFieldMeasurer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MagicFieldMeasurerBlock extends AbstractBlockContainer
{

	public MagicFieldMeasurerBlock()
	{
		super(DarkcraftMod.modName);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new MagicFieldMeasurer();
	}

	@Override
	public Class<? extends TileEntity> getTEClass()
	{
		return MagicFieldMeasurer.class;
	}

	@Override
	public void initData()
	{
		setBlockName("MagicFieldMeasurer");
	}

	@Override
	public void initRecipes()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public AbstractBlockRenderer getRenderer()
	{
		return new MagicFieldMeasurerRenderer();
	}

}
