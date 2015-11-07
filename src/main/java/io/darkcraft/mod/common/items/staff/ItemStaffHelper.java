package io.darkcraft.mod.common.items.staff;

import io.darkcraft.mod.common.items.staff.parts.StaffPartRegistry;
import io.darkcraft.mod.common.items.staff.parts.bottom.IStaffBottom;
import io.darkcraft.mod.common.items.staff.parts.head.IStaffHead;
import io.darkcraft.mod.common.items.staff.parts.shaft.IStaffShaft;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

public class ItemStaffHelper
{
	public static final String	nbtIDName	= "isID";

	private final int			id;
	private IStaffBottom		bottom		= StaffPartRegistry.getStaffBottom(null);
	private IStaffShaft			shaft		= StaffPartRegistry.getStaffShaft(null);
	private IStaffHead			head		= StaffPartRegistry.getStaffHead(null);

	public ItemStaffHelper(int _id)
	{
		id = _id;
	}

	public void render()
	{
		GL11.glPushMatrix();
		bottom.render();
		shaft.render();
		head.render();
		GL11.glPopMatrix();
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		if (!nbt.hasKey(nbtIDName) || (nbt.getInteger(nbtIDName) != id)) return;
		bottom	= StaffPartRegistry.getStaffBottom(nbt.getString("staffBottom"));
		shaft	= StaffPartRegistry.getStaffShaft(nbt.getString("staffShaft"));
		head	= StaffPartRegistry.getStaffHead(nbt.getString("staffHead"));
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger(nbtIDName, id);
		nbt.setString("staffBottom", bottom.getID());
		nbt.setString("staffShaft", shaft.getID());
		nbt.setString("staffHead", head.getID());
	}
}
