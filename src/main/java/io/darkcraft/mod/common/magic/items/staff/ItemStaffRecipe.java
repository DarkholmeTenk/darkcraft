package io.darkcraft.mod.common.magic.items.staff;

import io.darkcraft.mod.common.registries.ItemBlockRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ItemStaffRecipe extends ShapedOreRecipe
{
	public ItemStaffRecipe(ItemStack result, Object... recipe)
	{
		super(result, recipe);
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return new ItemStack(ItemBlockRegistry.itemStaff,1);
	}
}
