package io.darkcraft.mod.common.magic.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import io.darkcraft.mod.client.renderer.gui.system.DarkcraftGui.DummyContainer;
import io.darkcraft.mod.common.magic.items.MagicChalkNew;
import io.darkcraft.mod.common.magic.systems.symbolic.ChalkType;
import io.darkcraft.mod.common.network.ChalkGuiPacketHandler;

public class ChalkContainerNew extends DummyContainer
{
	public final ChalkType maxChalkLevel;
	public final String[] defaultStrings;
	private final EntityPlayer pl;

	public ChalkContainerNew(EntityPlayer pl)
	{
		this.pl = pl;
		ItemStack is = pl.getCurrentEquippedItem();
		if((is == null) || !(is.getItem() instanceof MagicChalkNew))
		{
			maxChalkLevel = ChalkType.WHITE;
			defaultStrings = new String[]{};
		}
		else
		{
			int damage = is.getItemDamage();
			maxChalkLevel = ChalkType.values()[damage % ChalkType.values().length];
			defaultStrings = MagicChalkNew.getStrings(is);
		}
	}

	public void update(String[] strings)
	{
		ChalkGuiPacketHandler.updateChalk(pl, strings);
	}
}
