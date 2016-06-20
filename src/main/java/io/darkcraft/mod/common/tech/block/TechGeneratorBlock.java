package io.darkcraft.mod.common.tech.block;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlockContainer;
import io.darkcraft.darkcore.mod.abstracts.AbstractBlockRenderer;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.renderer.tileent.TechGeneratorRenderer;
import io.darkcraft.mod.common.tech.tileent.TechGenerator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TechGeneratorBlock extends AbstractBlockContainer
{

	public TechGeneratorBlock()
	{
		super(false, DarkcraftMod.modName);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new TechGenerator();
	}

	@Override
	public Class<? extends TileEntity> getTEClass()
	{
		return TechGenerator.class;
	}

	@Override
	public void initData()
	{
		setBlockName("TechGenerator");
	}

	@Override
	public void initRecipes()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public AbstractBlockRenderer getRenderer()
	{
		return new TechGeneratorRenderer();
	}
}
