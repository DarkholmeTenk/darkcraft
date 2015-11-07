package io.darkcraft.mod.common.registries;

import io.darkcraft.mod.common.items.staff.ItemStaff;


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

	}
}
