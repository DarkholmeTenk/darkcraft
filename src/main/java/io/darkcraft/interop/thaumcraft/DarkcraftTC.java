package io.darkcraft.interop.thaumcraft;

import java.util.Collection;

import io.darkcraft.interop.thaumcraft.items.WandRodItem;
import io.darkcraft.interop.thaumcraft.magic.CleanWarp;
import io.darkcraft.interop.thaumcraft.magic.WarpWard;
import io.darkcraft.interop.thaumcraft.recipes.WandCapRecipe;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.systems.component.SpellPartRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ItemApi;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.api.wands.WandRod;

public class DarkcraftTC
{
	public static Item wand;

	public static WandRodItem rod;
	public static WandCapRecipe wcr;
	public static WandRod wr;
	public static ResearchItem rodResearch;
	public static void init()
	{
		SpellPartRegistry.registerComponent(new CleanWarp());
		SpellPartRegistry.registerComponent(new WarpWard());
		wand = ItemApi.getItem("itemWandCasting", 0).getItem();
		initCap();
	}

	private static void initCap()
	{
		rod = (WandRodItem) new WandRodItem().register();
		AspectList researchList = new AspectList();
		researchList.add(Aspect.LIGHT, 1);
		researchList.add(Aspect.MAGIC, 3);
		researchList.add(Aspect.CRYSTAL, 1);
		rodResearch = new ResearchItem("ROD_darkcraft","THAUMATURGY",researchList,-3,7,1,new ItemStack(rod));
		rodResearch.setParents("ROD_silverwood");
		ThaumcraftApi.getCraftingRecipes().add(wcr = new WandCapRecipe());
		rodResearch.setPages(new ResearchPage("darkcraft.tc4.page1"), new ResearchPage(wcr));


	}

	private static int getWandCapacity()
	{
		int best = -1;
		int sec = -1;
		Collection<WandRod> rods = WandRod.rods.values();
		for(WandRod wr : rods)
		{
			int cap = wr.getCapacity();
			if(cap > 1000) continue;
			if(cap > best)
				best = cap;
		}
		for(WandRod wr : rods)
		{
			int cap = wr.getCapacity();
			if((cap < best) && (cap > sec))
				sec = cap;
		}
		int t = (best + sec) / 2;
		return (t/25)*25;
	}

	public static void postInit()
	{
		wr = new WandRod("darkcraft", getWandCapacity(), new ItemStack(rod), 4, rod);
		wr.setGlowing(true);
		wr.setTexture(new ResourceLocation(DarkcraftMod.modName,"textures/items/tcRodTex.png"));
		rodResearch.registerResearchItem();
	}
}
