package io.darkcraft.mod.common.magic.items;

import io.darkcraft.darkcore.mod.abstracts.AbstractItem;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.registries.MagicAnvilRecipeRegistry;
import io.darkcraft.mod.common.registries.recipes.ScrollRecipe;

public class MagicScroll extends AbstractItem
{
	public MagicScroll()
	{
		super(DarkcraftMod.modName);
		setUnlocalizedName("MagicScroll");
	}

	@Override
	public void initRecipes()
	{
		MagicAnvilRecipeRegistry.addRecipe(new ScrollRecipe());
	}

}
