package io.darkcraft.mod.common.magic.blocks;

import java.util.List;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlockContainer;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.renderer.tileent.MagicLightRenderer;
import io.darkcraft.mod.common.magic.blocks.tileent.MagicLight;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class MagicLightBlock extends AbstractBlockContainer
{
	public MagicLightBlock()
	{
		super(false,DarkcraftMod.modName);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new MagicLight();
	}

	@Override
	public Class<? extends TileEntity> getTEClass()
	{
		return MagicLight.class;
	}

	@Override
	public void initData()
	{
		setBlockName("MagicLight");
		setLightLevel(1);
		setBlockBounds(0.3f, 0.4f, 0.3f, 0.7f, 0.6f, 0.7f);
		setHardness(0f);
	}

	@Override
	public void addCollisionBoxesToList(World w, int x, int y, int z, AxisAlignedBB aabb, List list, Entity ent)
	{
		return;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public boolean isBlockSolid(IBlockAccess w, int x, int y, int z, int s)
	{
		return false;
	}

	@Override
	public void initRecipes(){}

	@Override
	public Object getRenderer()
	{
		return new MagicLightRenderer();
	}

	@Override
	public boolean useRendererForItem()
	{
		return true;
	}

}
