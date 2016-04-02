package io.darkcraft.mod.common.magic.blocks;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlockContainer;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.tileent.MagicAnvil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
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
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
	{
		if (entity == null)
		{
			return;
		}

		int dir = MathHelper.floor_double(((entity.rotationYaw * 4.0F) / 360.0F) + 0.5D) & 1;
		world.setBlockMetadataWithNotify(x, y, z, dir, 3);
	}

	@Override
	public void initRecipes()
	{
		// TODO Auto-generated method stub

	}

}
