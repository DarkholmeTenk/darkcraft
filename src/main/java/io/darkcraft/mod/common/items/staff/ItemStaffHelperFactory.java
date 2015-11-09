package io.darkcraft.mod.common.items.staff;

import io.darkcraft.mod.DarkcraftMod;

import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemStaffHelperFactory
{
	private static HashMap<Integer, ItemStaffHelper>	map				= new HashMap();
	private static ItemStaffHelper						defaultHelper	= new ItemStaffHelper(0);

	private static int getNewID()
	{
		int id;
		while (map.containsKey(id = DarkcraftMod.modRand.nextInt()))
			;
		return id;
	}

	public static ItemStaffHelper getHelper(int id)
	{
		if (map.containsKey(id)) return map.get(id);
		ItemStaffHelper helper = new ItemStaffHelper(id);
		map.put(id, helper);
		return helper;
	}

	public static ItemStaffHelper getHelper(NBTTagCompound nbt)
	{
		if (nbt == null) return null;
		int id;
		if (nbt.hasKey(ItemStaffHelper.nbtIDName))
			id = nbt.getInteger(ItemStaffHelper.nbtIDName);
		else
		{
			id = getNewID();
			nbt.setInteger(ItemStaffHelper.nbtIDName, id);
		}
		ItemStaffHelper helper = getHelper(id);
		if((helper != null) && !helper.inited)
			helper.readFromNBT(nbt);
		return helper;
	}

	public static ItemStaffHelper getHelper(ItemStack is)
	{
		if ((is == null) || !(is.getItem() instanceof ItemStaff)) return null;
		if (is.stackTagCompound == null) is.stackTagCompound = new NBTTagCompound();
		ItemStaffHelper helper = getHelper(is.stackTagCompound);
		if(helper != null)
			helper.setIS(is);
		return helper;
	}

	public static ItemStaffHelper getDefaultHelper()
	{
		return defaultHelper;
	}
}
