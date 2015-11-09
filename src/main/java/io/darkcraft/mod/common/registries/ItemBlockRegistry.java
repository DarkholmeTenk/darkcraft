package io.darkcraft.mod.common.registries;

import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.items.staff.ItemStaff;
import io.darkcraft.mod.common.items.staff.parts.StaffPartRegistry;
import io.darkcraft.mod.common.items.staff.parts.bottom.DefaultStaffBottom;
import io.darkcraft.mod.common.items.staff.parts.head.DefaultStaffHead;
import io.darkcraft.mod.common.items.staff.parts.head.SillyStaffHead;
import io.darkcraft.mod.common.items.staff.parts.shaft.DefaultStaffShaft;
import net.minecraft.util.ResourceLocation;


public class ItemBlockRegistry
{
	public static ItemStaff itemStaff = new ItemStaff();
	public static void registerBlocks()
	{
	}

	public static void registerItems()
	{
		itemStaff.register();
		registerStaffParts();
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
