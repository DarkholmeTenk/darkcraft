package io.darkcraft.darkcraft.mod.common.registries;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlock;
import io.darkcraft.darkcore.mod.abstracts.AbstractItem;
import io.darkcraft.darkcraft.mod.common.blocks.PylonBlock;
import io.darkcraft.darkcraft.mod.common.blocks.PylonCore;
import io.darkcraft.darkcraft.mod.common.blocks.PylonPanel;
import io.darkcraft.darkcraft.mod.common.items.BaseStaff;

public class ItemBlockRegistry
{
	public static AbstractBlock	pylonCoreBlock;
	public static AbstractBlock	pylonPanelBlock;
	public static AbstractBlock	pylonBlock;

	public static AbstractItem	baseStaff;

	public static void registerBlocks()
	{
		pylonCoreBlock = new PylonCore().register();
		pylonPanelBlock = new PylonPanel().register();
		pylonBlock = new PylonBlock().register();
	}

	public static void registerItems()
	{
		baseStaff = new BaseStaff().register();
	}
}
