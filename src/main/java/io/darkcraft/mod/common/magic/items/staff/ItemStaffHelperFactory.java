package io.darkcraft.mod.common.magic.items.staff;

import io.darkcraft.mod.DarkcraftMod;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemStaffHelperFactory
{
	private static HashMap<Integer, ItemStaffHelper>				map				= new HashMap();
	private static HashMap<Integer, Long>							accessMap		= new HashMap();
	private static long												lastClearTime	= 0;
	private static HashMap<Integer, WeakReference<ItemStaffHelper>>	weakMap			= new HashMap();
	private static ItemStaffHelper									defaultHelper	= new ItemStaffHelper(0);

	private static int getNewID()
	{
		int id;
		while (map.containsKey(id = DarkcraftMod.modRand.nextInt()))
			;
		return id;
	}

	public static void clear()
	{
		map.clear();
		accessMap.clear();
		weakMap.clear();
		lastClearTime = 0;
	}

	private static void clearOldStuff()
	{
		if(lastClearTime >= (System.currentTimeMillis() - 10000)) return;
		lastClearTime = System.currentTimeMillis();
		Iterator<Integer> iter = weakMap.keySet().iterator();
		while(iter.hasNext())
		{
			Integer id = iter.next();
			WeakReference<ItemStaffHelper> item = weakMap.get(id);
			if(item.get() == null)
				iter.remove();
		}
		iter = accessMap.keySet().iterator();
		while(iter.hasNext())
		{
			Integer id = iter.next();
			long time = accessMap.get(id);
			if(time >= (System.currentTimeMillis() - 10000)) continue;
			map.remove(id);
			iter.remove();
		}
	}

	public static ItemStaffHelper getHelper(int id)
	{
		clearOldStuff();
		accessMap.put(id, System.currentTimeMillis());
		if (map.containsKey(id)) return map.get(id);
		ItemStaffHelper helper = null;
		if(weakMap.containsKey(id))
			helper = weakMap.get(id).get();
		if(helper == null)
		{
			helper = new ItemStaffHelper(id);
			weakMap.put(id, new WeakReference(helper));
		}
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
