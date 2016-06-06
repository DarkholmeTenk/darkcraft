package io.darkcraft.mod.common.registries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import gnu.trove.set.hash.THashSet;
import io.darkcraft.api.magic.IMagicAnvilRecipe;
import io.darkcraft.mod.DarkcraftMod;

public class MagicAnvilRecipeRegistry
{
	private static List<IMagicAnvilRecipe> recipeList = new ArrayList();
	private static Set<IMagicAnvilRecipe> recipes = new THashSet();
	private static List<IMagicAnvilRecipe> unmod;

	public static void addRecipe(IMagicAnvilRecipe rec)
	{
		if(rec == null)
			throw new RuntimeException("Null recipe added");
		if(DarkcraftMod.inited)
			throw new RuntimeException("Already initialised recipes");
		recipes.add(rec);
		recipeList.add(rec);
	}

	public static List<IMagicAnvilRecipe> getRecipes()
	{
		if(DarkcraftMod.inited)
			return unmod;
		return null;
	}

	public static int getNumRecipes()
	{
		return recipes.size();
	}

	public static IMagicAnvilRecipe getRecipe(int num)
	{
		if(DarkcraftMod.inited)
		{
			return unmod.get(num);
		}
		else
			System.err.println("Recipes not inited, can't get recipe yet");
		return null;
	}

	public static void postInit()
	{
		List<IMagicAnvilRecipe> recipeList = new ArrayList(recipes);
		recipeList.sort(new Comparator<IMagicAnvilRecipe>(){
			@Override
			public int compare(IMagicAnvilRecipe a,IMagicAnvilRecipe b)
			{
				return a.id().compareTo(b.id());
			}
		});
		unmod = Collections.unmodifiableList(recipeList);
	}
}
