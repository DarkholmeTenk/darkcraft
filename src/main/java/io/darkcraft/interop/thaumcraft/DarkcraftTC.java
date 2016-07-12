package io.darkcraft.interop.thaumcraft;

import java.util.Collection;

import io.darkcraft.darkcore.mod.config.ConfigFile;
import io.darkcraft.darkcore.mod.interop.InteropInstance;
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

public class DarkcraftTC extends InteropInstance
{
	public DarkcraftTC()
	{
		super("Thaumcraft");
	}

	public static Item wand;
	public static WandRodItem rod;
	public static WandCapRecipe wcr;
	public static WandRod wr;
	public static ResearchItem rodResearch;
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
		return Math.min((t/25)*25, maxAspects);
	}

	public ConfigFile config;
	public static int maxAspects;
	public static int maxRecharge;

	@Override
	public void preInit()
	{
		config = DarkcraftMod.configHandler.registerConfigNeeder("thaumcraft");
		maxAspects = config.getInt("Wand rod - max aspects", 250,
				"The maximum of each aspect that the wand core will set itself to",
				"It will try to sit between the largest and second largest rod, unless it exceeds this value");
		maxRecharge = config.getInt("Wand rod - max recharge", 100, "The maximum amount of aspects that the wand core can gain by mana recharging");
	}

	@Override
	public void init()
	{
		SpellPartRegistry.registerComponent(new CleanWarp());
		SpellPartRegistry.registerComponent(new WarpWard());
		wand = ItemApi.getItem("itemWandCasting", 0).getItem();
		initCap();
	}

	@Override
	public void postInit()
	{
		wr = new WandRod("darkcraft", getWandCapacity(), new ItemStack(rod), 4, rod);
		wr.setGlowing(true);
		wr.setTexture(new ResourceLocation(DarkcraftMod.modName,"textures/items/tcRodTex.png"));
		rodResearch.registerResearchItem();
	}
}
