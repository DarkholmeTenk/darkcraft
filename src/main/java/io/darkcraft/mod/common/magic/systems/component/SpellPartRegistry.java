package io.darkcraft.mod.common.magic.systems.component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Register your spell components using this class.
 * @author dark
 *
 */
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

	public static Set<IComponent> getAllComponents()
	{
		HashSet<IComponent> comps = new HashSet();
		for(IComponent c : compMap.values())
			comps.add(c);
		return comps;
	}
}
