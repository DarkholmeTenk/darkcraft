package io.darkcraft.mod.common.magic.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlockContainer;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.renderer.tileent.GemStandRenderer;
import io.darkcraft.mod.common.magic.blocks.tileent.GemStand;
import io.darkcraft.mod.common.magic.items.MagicComponent;

import cpw.mods.fml.common.registry.GameRegistry;

public class GemStandBlock extends AbstractBlockContainer
{
	public GemStandBlock()
	{
		super(false,DarkcraftMod.modName);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new GemStand();
	}

	@Override
	public Class<? extends TileEntity> getTEClass()
	{
		return GemStand.class;
	}

	@Override
	public void initData()
	{
		setBlockName("GemStand");
		setBlockBounds(0.4f,0,0.4f,0.6f,0.9f,0.6f);
		setHardness(3.0f);
	}

	@Override
	public void initRecipes()
	{
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this), false, " g ","nin","nin",
				'g', MagicComponent.Type.Crystal.getIS(1),
				'n', "nuggetGold",
				'i', "ingotIron"));
	}

	@Override
	public Object getRenderer()
	{
		return new GemStandRenderer();
	}

	@Override
	public boolean useRendererForItem()
	{
		return true;
	}

}
