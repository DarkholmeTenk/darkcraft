package io.darkcraft.mod.common.magic.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import io.darkcraft.darkcore.mod.abstracts.AbstractBlockContainer;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.tileent.MagicGuide;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class MagicGuideBlock extends AbstractBlockContainer
{
	public MagicGuideBlock()
	{
		super(false, DarkcraftMod.modName);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new MagicGuide();
	}

	@Override
	public Class<? extends TileEntity> getTEClass()
	{
		return MagicGuide.class;
	}

	@Override
	public void initData()
	{
		setBlockName("MagicGuide");
		setHardness(3.0f);
		minX = 0.21;
		maxX = 0.79;
		minZ = 0.21;
		maxZ = 0.79;
	}

	@Override
	public void initRecipes()
	{
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this,1), false, " b ","www","wgw",
				'b', Items.book,
				'w', "plankWood",
				'g', "ingotGold"));
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
	{
		if (entity == null)
		{
			return;
		}

		int dir = MathHelper.floor_double(((entity.rotationYaw * 4.0F) / 360.0F) + 0.5D) & 3;
		world.setBlockMetadataWithNotify(x, y, z, dir, 3);
	}

}
