package io.darkcraft.mod.common.magic;

import io.darkcraft.mod.common.magic.component.IComponent;

import java.util.HashMap;

public class SpellPartRegistry
{
	private static HashMap<String, IComponent>	compMap	= new HashMap();

	public static void registerComponent(IComponent comp)
	{
		String id = comp.id();
		compMap.put(id, comp);
	}

	public static IComponent getComponent(String id)
	{
		return compMap.get(id);
	}
}
