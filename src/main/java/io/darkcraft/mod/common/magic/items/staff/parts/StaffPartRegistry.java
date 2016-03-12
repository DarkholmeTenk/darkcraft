package io.darkcraft.mod.common.magic.items.staff.parts;

import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.items.staff.parts.bottom.IStaffBottom;
import io.darkcraft.mod.common.magic.items.staff.parts.head.IStaffHead;
import io.darkcraft.mod.common.magic.items.staff.parts.shaft.IStaffShaft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StaffPartRegistry
{
	public static HashMap<String, IStaffPart>	heads		= new HashMap();
	public static HashMap<String, IStaffPart>	shafts		= new HashMap();
	public static HashMap<String, IStaffPart>	bottoms		= new HashMap();
	public static ArrayList<String>				headNames	= new ArrayList();
	public static ArrayList<String>				shaftNames	= new ArrayList();
	public static ArrayList<String>				bottomNames	= new ArrayList();
	public static ArrayList<String>				headDNames	= new ArrayList();
	public static ArrayList<String>				shaftDNames	= new ArrayList();
	public static ArrayList<String>				bottomDNames	= new ArrayList();

	private static IStaffPart getStaffPart(String id, HashMap<String, IStaffPart> partMap, List<String> names, List<String> dNames)
	{
		if ((id != null) && partMap.containsKey(id)) return partMap.get(id);
		int size = dNames.size();
		int index = DarkcraftMod.modRand.nextInt(size);
		String newId = dNames.get(index);
		return partMap.get(newId);
	}

	public static List<String> getStaffBottomNames(boolean def)
	{
		return def ? bottomDNames : bottomNames;
	}

	public static List<String> getStaffShaftNames(boolean def)
	{
		return def ? shaftDNames : shaftNames;
	}

	public static List<String> getStaffHeadNames(boolean def)
	{
		return def ? headDNames : headNames;
	}

	public static List<String> getStaffPartNames(StaffPartType type, boolean def)
	{
		switch(type)
		{
			case HEAD: return getStaffHeadNames(def);
			case SHAFT: return getStaffShaftNames(def);
			case BOTTOM: return getStaffBottomNames(def);
		}
		return null;
	}

	public static IStaffBottom getStaffBottom(String id)
	{
		return (IStaffBottom) getStaffPart(id, bottoms, bottomNames, bottomDNames);
	}

	public static IStaffShaft getStaffShaft(String id)
	{
		return (IStaffShaft) getStaffPart(id, shafts, shaftNames, shaftDNames);
	}

	public static IStaffHead getStaffHead(String id)
	{
		return (IStaffHead) getStaffPart(id, heads, headNames, headDNames);
	}

	public static IStaffPart getStaffPart(StaffPartType type, String id)
	{
		switch(type)
		{
			case HEAD: return getStaffHead(id);
			case SHAFT: return getStaffShaft(id);
			case BOTTOM: return getStaffBottom(id);
		}
		return null;
	}

	private static void add(String id, IStaffPart part, HashMap<String,IStaffPart> map, List<String> names, List<String> defaultNames, boolean isDefault)
	{
		map.put(id, part);
		names.add(id);
		if(isDefault)
			defaultNames.add(id);
	}

	public static void register(IStaffPart part)
	{
		String id = part.id();
		boolean isDef = part.isDefault();
		if (part instanceof IStaffHead) add(id, part, heads, headNames, headDNames, isDef);
		if (part instanceof IStaffShaft) add(id, part, shafts, shaftNames, shaftDNames, isDef);
		if (part instanceof IStaffBottom) add(id, part, bottoms, bottomNames, bottomDNames, isDef);
	}
}
