package io.darkcraft.mod.common.registries;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class DarkcraftCreativeTabs extends CreativeTabs
{

	public DarkcraftCreativeTabs()
	{
		super("darkcraft");
	}

	@Override
	public Item getTabIconItem()
	{
		return ItemBlockRegistry.itemStaff;
	}

}
