package io.darkcraft.mod.common.magic.blocks;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlockContainer;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.blocks.tileent.MagicInventory;
import io.darkcraft.mod.common.magic.items.MagicComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class MagicInventoryBlock extends AbstractBlockContainer
{

	public MagicInventoryBlock()
	{
		super(true, true, DarkcraftMod.modName);
		setHardness(1.5F);
	}

	@Override
	public void initData()
	{
		setBlockName("MagicInventory");
	}

	@Override
	public void initRecipes()
	{
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this,1), false, "www","wcw","www",
				'w', "plankWood",
				'c', MagicComponent.Type.Crystal.getIS(1)));
	}

	@Override
	public TileEntity createNewTileEntity(World w, int m)
	{
		return new MagicInventory(w);
	}

	@Override
	public Class<? extends TileEntity> getTEClass()
	{
		return MagicInventory.class;
	}
}
