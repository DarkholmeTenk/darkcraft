package io.darkcraft.mod.common.magic.items.staff;

import io.darkcraft.mod.common.magic.items.staff.parts.IStaffPart;
import io.darkcraft.mod.common.magic.items.staff.parts.StaffPartRegistry;
import io.darkcraft.mod.common.magic.items.staff.parts.StaffPartType;
import io.darkcraft.mod.common.magic.items.staff.parts.bottom.IStaffBottom;
import io.darkcraft.mod.common.magic.items.staff.parts.head.IStaffHead;
import io.darkcraft.mod.common.magic.items.staff.parts.shaft.IStaffShaft;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;

import java.lang.ref.WeakReference;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

public class StaffHelper
{
	public static final String	nbtIDName	= "isID";

	private final int					id;
	private IStaffBottom				bottom		= StaffPartRegistry.getStaffBottom(null);
	private IStaffShaft					shaft		= StaffPartRegistry.getStaffShaft(null);
	private IStaffHead					head		= StaffPartRegistry.getStaffHead(null);
	private String						displayName;
	private WeakReference<ItemStack>	itemstack;
	public boolean						inited = false;
	public NBTTagCompound				extraNBT = new NBTTagCompound();

	public StaffHelper(int _id)
	{
		id = _id;
	}

	public ItemStack getIS() { return getIS(false); }

	public ItemStack getIS(boolean spawnNew)
	{
		if((itemstack == null) || (itemstack.get() == null))
		{
			if(!spawnNew)
				return null;
			ItemStack is = new ItemStack(ItemBlockRegistry.itemStaff, 1);
			if(displayName != null)
				is.setStackDisplayName(displayName);
			setIS(is);
			return is;
		}
		return itemstack.get();
	}

	public void setIS(ItemStack newIS)
	{
		boolean newName = newIS.hasDisplayName() && !(newIS.getDisplayName().equals(displayName));
		if((getIS() == newIS) && !newName)
			return;
		itemstack = new WeakReference<ItemStack>(newIS);
		if(newName)
			displayName = newIS.getDisplayName();
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
	public void setStaffPart(IStaffPart part, StaffPartType type)
	{
		switch(type)
		{
			case HEAD: setStaffHead((IStaffHead)part); break;
			case SHAFT: setStaffShaft((IStaffShaft)part); break;
			case BOTTOM: setStaffBottom((IStaffBottom)part); break;
		}
	}

	public IStaffPart getStaffPart(StaffPartType type)
	{
		switch(type)
		{
			case HEAD: return head;
			case SHAFT: return shaft;
			case BOTTOM: return bottom;
		}
		return null;
	}

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
		displayName = nbt.hasKey("display") ? nbt.getString("display") : null;
		if(!nbt.hasKey("extra"))
			extraNBT = new NBTTagCompound();
		else
			extraNBT = nbt.getCompoundTag("extra");
		inited = true;
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger(nbtIDName, id);
		nbt.setString("staffBottom", bottom.id());
		nbt.setString("staffShaft", shaft.id());
		nbt.setString("staffHead", head.id());
		if(displayName != null)
			nbt.setString("display", displayName);
		nbt.setTag("extra", extraNBT);
	}

	public void addInfo(List<String> list, EntityPlayer pl)
	{
	}
}
