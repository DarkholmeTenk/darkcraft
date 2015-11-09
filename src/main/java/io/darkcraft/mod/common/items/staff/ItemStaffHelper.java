package io.darkcraft.mod.common.items.staff;

import io.darkcraft.mod.common.items.staff.parts.StaffPartRegistry;
import io.darkcraft.mod.common.items.staff.parts.bottom.IStaffBottom;
import io.darkcraft.mod.common.items.staff.parts.head.IStaffHead;
import io.darkcraft.mod.common.items.staff.parts.shaft.IStaffShaft;

import java.lang.ref.WeakReference;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

public class ItemStaffHelper
{
	public static final String	nbtIDName	= "isID";

	private final int					id;
	private IStaffBottom				bottom		= StaffPartRegistry.getStaffBottom(null);
	private IStaffShaft					shaft		= StaffPartRegistry.getStaffShaft(null);
	private IStaffHead					head		= StaffPartRegistry.getStaffHead(null);
	private WeakReference<ItemStack>	itemstack;
	public boolean						inited = false;

	public ItemStaffHelper(int _id)
	{
		id = _id;
	}

	public ItemStack getIS()
	{
		if(itemstack == null)
			return null;
		return itemstack.get();
	}

	public void setIS(ItemStack newIS)
	{
		if(getIS() == newIS)
			return;
		itemstack = new WeakReference<ItemStack>(newIS);
		markDirty();
	}

	public void render()
	{
		GL11.glPushMatrix();
		bottom.render();
		shaft.render();
		head.render();
		GL11.glPopMatrix();
	}

	public void setStaffBottom(IStaffBottom part)	{ if((part==null) || (part == bottom)) return; bottom = part; markDirty(); }
	public void setStaffShaft(IStaffShaft part)		{ if((part==null) || (part == shaft)) return; shaft = part; markDirty(); }
	public void setStaffHead(IStaffHead part)		{ if((part==null) || (part == head)) return; head = part; markDirty(); }

	public void markDirty()
	{
		ItemStack is = getIS();
		if(is != null)
		{
			if(is.stackTagCompound == null)
				is.stackTagCompound = new NBTTagCompound();
			writeToNBT(is.stackTagCompound);
		}
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		if (!nbt.hasKey(nbtIDName) || (nbt.getInteger(nbtIDName) != id)) return;
		bottom	= StaffPartRegistry.getStaffBottom(nbt.getString("staffBottom"));
		shaft	= StaffPartRegistry.getStaffShaft(nbt.getString("staffShaft"));
		head	= StaffPartRegistry.getStaffHead(nbt.getString("staffHead"));

		inited = true;
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger(nbtIDName, id);
		nbt.setString("staffBottom", bottom.getID());
		nbt.setString("staffShaft", shaft.getID());
		nbt.setString("staffHead", head.getID());
	}
}
