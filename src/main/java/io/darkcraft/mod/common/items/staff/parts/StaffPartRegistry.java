package io.darkcraft.mod.common.items.staff.parts;

import io.darkcraft.mod.common.items.staff.parts.bottom.IStaffBottom;
import io.darkcraft.mod.common.items.staff.parts.head.IStaffHead;
import io.darkcraft.mod.common.items.staff.parts.shaft.IStaffShaft;

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

	private static IStaffPart getStaffPart(String id, HashMap<String, IStaffPart> partMap, List<String> names)
	{
		if ((id != null) && partMap.containsKey(id)) return partMap.get(id);
		return partMap.get(names.get(0));
	}

	public static IStaffBottom getStaffBottom(String id)
	{
		return (IStaffBottom) getStaffPart(id, bottoms, bottomNames);
	}

	public static IStaffShaft getStaffShaft(String id)
	{
		return (IStaffShaft) getStaffPart(id, shafts, shaftNames);
	}

	public static IStaffHead getStaffHead(String id)
	{
		return (IStaffHead) getStaffPart(id, heads, headNames);
	}

	private static void add(String id, IStaffPart part, HashMap<String,IStaffPart> map, List<String> names)
	{
		map.put(id, part);
		names.add(id);
	}

	public static void register(IStaffPart part)
	{
		String id = part.getID();
		if (part instanceof IStaffHead) add(id, part, heads, headNames);
		if (part instanceof IStaffShaft) add(id, part, shafts, shaftNames);
		if (part instanceof IStaffBottom) add(id, part, bottoms, bottomNames);
	}
}
