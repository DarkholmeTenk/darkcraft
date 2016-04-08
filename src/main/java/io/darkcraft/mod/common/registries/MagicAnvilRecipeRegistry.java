package io.darkcraft.mod.common.registries;

import gnu.trove.set.hash.THashSet;
import io.darkcraft.api.magic.IMagicAnvilRecipe;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.mod.DarkcraftMod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MagicAnvilRecipeRegistry
{
	private static List<IMagicAnvilRecipe> recipeList = new ArrayList();
	private static Set<IMagicAnvilRecipe> recipes = new THashSet();
	private static Set<IMagicAnvilRecipe> unmod = Collections.unmodifiableSet(recipes);
	private static IMagicAnvilRecipe[] recipeArray = null;

	public static void addRecipe(IMagicAnvilRecipe rec)
	{
		if(rec == null)
			throw new RuntimeException("Null recipe added");
		if(DarkcraftMod.inited)
			throw new RuntimeException("Already initialised recipes");
		recipes.add(rec);
		recipeList.add(rec);
	}

	public static Set<IMagicAnvilRecipe> getRecipes()
	{
		if(DarkcraftMod.inited)
			return unmod;
		return recipes;
	}

	public static int getNumRecipes()
	{
		return recipes.size();
	}

	public static IMagicAnvilRecipe getRecipe(int num)
	{
		if(DarkcraftMod.inited)
		{
			if(recipeArray == null)
			{
				recipeArray = new IMagicAnvilRecipe[recipes.size()];
				int i = 0;
				for(IMagicAnvilRecipe rec : recipeList)
					recipeArray[i++] = rec;
			}
			num = MathHelper.clamp(num, 0, recipeArray.length-1);
			return recipeArray[num];
		}
		else
			System.err.println("Recipes not inited, can't get recipe yet");
		return null;
	}
}
