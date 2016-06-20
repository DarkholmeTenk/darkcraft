package io.darkcraft.mod.common.magic.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import io.darkcraft.darkcore.mod.abstracts.AbstractBlockContainer;
import io.darkcraft.darkcore.mod.abstracts.AbstractBlockRenderer;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.renderer.tileent.SpellCreatorRenderer;
import io.darkcraft.mod.common.magic.blocks.tileent.SpellCreator;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class SpellCreatorBlock extends AbstractBlockContainer
{
	public SpellCreatorBlock()
	{
		super(false,DarkcraftMod.modName);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new SpellCreator();
	}

	@Override
	public Class<? extends TileEntity> getTEClass()
	{
		return SpellCreator.class;
	}

	@Override
	public void initData()
	{
		setBlockName("SpellCreator");
		setHardness(4.0f);
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public void initRecipes()
	{
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this,1), false, "ldl","www","wgw",
				'l', "blockGlass",
				'd', "blockCoal",
				'w', "plankWood",
				'g', "ingotGold"));
	}

	@Override
	public AbstractBlockRenderer getRenderer()
	{
		return new SpellCreatorRenderer();
	}

	@Override
	public boolean useRendererForItem()
	{
		return true;
	}

}
