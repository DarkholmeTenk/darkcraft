package io.darkcraft.mod.common.magic.systems.component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import io.darkcraft.darkcore.mod.config.ConfigFile;
import io.darkcraft.mod.DarkcraftMod;

/**
 * Register your spell components using this class.
 * @author dark
 *
 */
public class SpellPartRegistry
{
	private static ConfigFile config;
	private static HashMap<String, IComponent>	compMap	= new HashMap();

	static
	{
		config = DarkcraftMod.configHandler.registerConfigNeeder("componentRegistry");
	}

	public static void registerComponent(IComponent comp, boolean defaultEnabled)
	{
		String id = comp.id();
		if(config.getBoolean("Enabled - " + id, defaultEnabled, "If false, this component will not be registered"))
			compMap.put(id, comp);
	}

	public static void registerComponent(IComponent comp)
	{
		registerComponent(comp, true);
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
