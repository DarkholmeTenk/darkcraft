package io.darkcraft.mod.common.magic.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import io.darkcraft.darkcore.mod.abstracts.AbstractBlockContainer;
import io.darkcraft.darkcore.mod.abstracts.AbstractBlockRenderer;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.renderer.tileent.MagicAnvilRenderer;
import io.darkcraft.mod.common.magic.blocks.tileent.MagicAnvil;
import io.darkcraft.mod.common.magic.items.MagicComponent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

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
		setHardness(5.0f);
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
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this,1), false, "gig","ici","iii",
				'i', "ingotIron",
				'g', "ingotGold",
				'c', MagicComponent.Type.Crystal.getIS(1)));
	}

	@Override
	public AbstractBlockRenderer getRenderer()
	{
		return new MagicAnvilRenderer();
	}

	@Override
	public boolean useRendererForItem()
	{
		return true;
	}

}
