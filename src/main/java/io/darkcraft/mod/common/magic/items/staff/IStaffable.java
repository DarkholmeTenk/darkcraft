package io.darkcraft.mod.common.magic.items.staff;

import net.minecraft.entity.player.EntityPlayer;

public interface IStaffable
{
	public boolean staffActivate(EntityPlayer player, ItemStaffHelper helper);
}
