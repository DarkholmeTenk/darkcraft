package io.darkcraft.mod.common.registries;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlock;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.blocks.MagicFieldMeasurerBlock;
import io.darkcraft.mod.common.magic.blocks.MagicInventoryBlock;
import io.darkcraft.mod.common.magic.blocks.MagicStaffChangerBlock;
import io.darkcraft.mod.common.magic.blocks.MagicTouchPassBlock;
import io.darkcraft.mod.common.magic.blocks.MagicVortexBlock;
import io.darkcraft.mod.common.magic.blocks.MagicVortexCrystalBlock;
import io.darkcraft.mod.common.magic.items.MagicComponent;
import io.darkcraft.mod.common.magic.items.staff.ItemStaff;
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
	public static AbstractBlock magicTouchPass;
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
		//TECH
		techGenerator = new TechGeneratorBlock().register();
	}

	public static MagicComponent magicComponent = new MagicComponent();
	public static ItemStaff itemStaff = new ItemStaff();
	public static void registerItems()
	{
		magicComponent.register();
		itemStaff.register();
		registerStaffParts();
	}

	public static void registerRecipes()
	{
		magicInventory.initRecipes();
		magicComponent.initRecipes();
		itemStaff.initRecipes();
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
