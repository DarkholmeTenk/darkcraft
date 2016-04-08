package io.darkcraft.api.magic;

import io.darkcraft.mod.common.magic.items.staff.StaffHelper;
import net.minecraft.entity.player.EntityPlayer;

public interface IStaffable
{
	public boolean staffActivate(EntityPlayer player, StaffHelper helper);
}
