package io.darkcraft.mod.common.registries;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlock;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.blocks.GemStandBlock;
import io.darkcraft.mod.common.magic.blocks.MagicAnvilBlock;
import io.darkcraft.mod.common.magic.blocks.MagicFieldMeasurerBlock;
import io.darkcraft.mod.common.magic.blocks.MagicGuideBlock;
import io.darkcraft.mod.common.magic.blocks.MagicInventoryBlock;
import io.darkcraft.mod.common.magic.blocks.MagicLightBlock;
import io.darkcraft.mod.common.magic.blocks.MagicStaffChangerBlock;
import io.darkcraft.mod.common.magic.blocks.MagicSymbolBlock;
import io.darkcraft.mod.common.magic.blocks.MagicTouchPassBlock;
import io.darkcraft.mod.common.magic.blocks.MagicVortexBlock;
import io.darkcraft.mod.common.magic.blocks.MagicVortexCrystalBlock;
import io.darkcraft.mod.common.magic.blocks.SpellCreatorBlock;
import io.darkcraft.mod.common.magic.items.ComponentBook;
import io.darkcraft.mod.common.magic.items.MagicChalk;
import io.darkcraft.mod.common.magic.items.MagicComponent;
import io.darkcraft.mod.common.magic.items.MagicScroll;
import io.darkcraft.mod.common.magic.items.SoulGem;
import io.darkcraft.mod.common.magic.items.staff.Staff;
import io.darkcraft.mod.common.magic.items.staff.parts.StaffPartRegistry;
import io.darkcraft.mod.common.magic.items.staff.parts.bottom.DefaultStaffBottom;
import io.darkcraft.mod.common.magic.items.staff.parts.head.DefaultStaffHead;
import io.darkcraft.mod.common.magic.items.staff.parts.head.SillyStaffHead;
import io.darkcraft.mod.common.magic.items.staff.parts.shaft.DefaultStaffShaft;
import io.darkcraft.mod.common.tech.block.TechGeneratorBlock;
import net.minecraft.util.ResourceLocation;


public class ItemBlockRegistry
{
	public static AbstractBlock magicFieldMeasurer;
	public static AbstractBlock magicVortex;
	public static AbstractBlock magicVortexCrystal;
	public static AbstractBlock magicInventory;
	public static AbstractBlock magicStaffChanger;
	public static AbstractBlock spellCreatorBlock;
	public static AbstractBlock magicTouchPass;
	public static AbstractBlock magicAnvil;
	public static AbstractBlock magicGuide;
	public static AbstractBlock magicLight;
	public static AbstractBlock magicSymbol;
	public static AbstractBlock gemStand;
	public static AbstractBlock techGenerator;
	public static void registerBlocks()
	{
		//MAGIC
		magicTouchPass = new MagicTouchPassBlock().register();

		magicInventory = new MagicInventoryBlock().register();
		magicFieldMeasurer = new MagicFieldMeasurerBlock().register();
		magicVortex = new MagicVortexBlock().register();
		magicVortexCrystal = new MagicVortexCrystalBlock().register();
		magicStaffChanger = new MagicStaffChangerBlock().register();
		spellCreatorBlock = new SpellCreatorBlock().register();
		magicAnvil = new MagicAnvilBlock().register();
		magicGuide = new MagicGuideBlock().register();
		magicLight = new MagicLightBlock().register();
		magicSymbol = new MagicSymbolBlock().register();
		gemStand = new GemStandBlock().register();
		//TECH
		techGenerator = new TechGeneratorBlock().register();
	}

	public static MagicComponent magicComponent = new MagicComponent();
	public static Staff itemStaff = new Staff();
	public static SoulGem soulGem = new SoulGem();
	public static MagicScroll scroll = new MagicScroll();
	public static ComponentBook compBook = new ComponentBook();
	public static MagicChalk magicChalk = new MagicChalk();
	public static void registerItems()
	{
		magicComponent.register();
		itemStaff.register();
		soulGem.register();
		scroll.register();
		compBook.register();
		magicChalk.register();
		registerStaffParts();
	}

	public static void registerRecipes()
	{
		/*magicInventory.initRecipes();
		magicComponent.initRecipes();
		itemStaff.initRecipes();
		scroll.initRecipes();
		soulGem.initRecipes();*/
	}

	private static ResourceLocation getRL(String pos)
	{
		return new ResourceLocation(DarkcraftMod.modName, pos);
	}

	private static void registerStaffParts()
	{
		StaffPartRegistry.register(new DefaultStaffShaft("DefaultShaft"));
		StaffPartRegistry.register(new DefaultStaffShaft("DefaultShaft2", getRL("textures/staff/defaultShaftTwo.png")));
		StaffPartRegistry.register(new DefaultStaffBottom("DefaultBottom"));
		StaffPartRegistry.register(new DefaultStaffBottom("DefaultBottom2", getRL("textures/staff/defaultBottomTwo.png")));
		StaffPartRegistry.register(new DefaultStaffHead("DefaultHeadB"));
		StaffPartRegistry.register(new DefaultStaffHead("DefaultHeadR", getRL("textures/staff/defaultHeadRed.png")));
		StaffPartRegistry.register(new DefaultStaffHead("DefaultHeadG", getRL("textures/staff/defaultHeadGreen.png")));
		StaffPartRegistry.register(new SillyStaffHead("SillyHeadB"));
	}
}
