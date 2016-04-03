package io.darkcraft.mod.common.registries;

import gnu.trove.set.hash.THashSet;
import io.darkcraft.api.magic.IMagicAnvilRecipe;
import io.darkcraft.mod.DarkcraftMod;

import java.util.Collections;
import java.util.Set;

public class MagicAnvilRecipeRegistry
{
	private static Set<IMagicAnvilRecipe> recipes = new THashSet();
	private static Set<IMagicAnvilRecipe> unmod = Collections.unmodifiableSet(recipes);

	public static void addRecipe(IMagicAnvilRecipe rec)
	{
		if(rec == null)
			throw new RuntimeException("Null recipe added");
		if(DarkcraftMod.inited)
			throw new RuntimeException("Already initialised recipes");
		recipes.add(rec);
	}

	public static Set<IMagicAnvilRecipe> getRecipes()
	{
		if(DarkcraftMod.inited)
			return unmod;
		return recipes;
	}
}
