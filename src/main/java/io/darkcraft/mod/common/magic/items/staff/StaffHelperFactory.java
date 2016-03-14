package io.darkcraft.mod.common.magic.items.staff;

import io.darkcraft.mod.DarkcraftMod;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class StaffHelperFactory
{
	private static HashMap<Integer, StaffHelper>				map				= new HashMap();
	private static HashMap<Integer, Long>							accessMap		= new HashMap();
	private static long												lastClearTime	= 0;
	private static HashMap<Integer, WeakReference<StaffHelper>>	weakMap			= new HashMap();
	private static StaffHelper									defaultHelper	= new StaffHelper(0);

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
			WeakReference<StaffHelper> item = weakMap.get(id);
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

	public static StaffHelper getHelper(int id)
	{
		clearOldStuff();
		accessMap.put(id, System.currentTimeMillis());
		if (map.containsKey(id)) return map.get(id);
		StaffHelper helper = null;
		if(weakMap.containsKey(id))
			helper = weakMap.get(id).get();
		if(helper == null)
		{
			helper = new StaffHelper(id);
			weakMap.put(id, new WeakReference(helper));
		}
		map.put(id, helper);
		return helper;
	}

	public static StaffHelper getHelper(NBTTagCompound nbt)
	{
		if (nbt == null) return null;
		int id;
		if (nbt.hasKey(StaffHelper.nbtIDName))
			id = nbt.getInteger(StaffHelper.nbtIDName);
		else
		{
			id = getNewID();
			nbt.setInteger(StaffHelper.nbtIDName, id);
		}
		StaffHelper helper = getHelper(id);
		if((helper != null) && !helper.inited)
			helper.readFromNBT(nbt);
		return helper;
	}

	public static StaffHelper getHelper(ItemStack is)
	{
		if ((is == null) || !(is.getItem() instanceof Staff)) return null;
		if (is.stackTagCompound == null) is.stackTagCompound = new NBTTagCompound();
		StaffHelper helper = getHelper(is.stackTagCompound);
		if(helper != null)
			helper.setIS(is);
		return helper;
	}

	public static StaffHelper getDefaultHelper()
	{
		return defaultHelper;
	}
}
