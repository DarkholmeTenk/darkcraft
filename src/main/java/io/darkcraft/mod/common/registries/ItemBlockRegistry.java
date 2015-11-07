package io.darkcraft.mod.common.registries;

import io.darkcraft.mod.common.items.staff.ItemStaff;
import io.darkcraft.mod.common.items.staff.parts.StaffPartRegistry;
import io.darkcraft.mod.common.items.staff.parts.bottom.DefaultStaffBottom;
import io.darkcraft.mod.common.items.staff.parts.head.DefaultStaffHead;
import io.darkcraft.mod.common.items.staff.parts.shaft.DefaultStaffShaft;


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

	private static void registerStaffParts()
	{
		StaffPartRegistry.register(new DefaultStaffShaft("DefaultShaft"));
		StaffPartRegistry.register(new DefaultStaffBottom("DefaultBottom"));
		StaffPartRegistry.register(new DefaultStaffHead("DefaultHead"));
	}
}
