package io.darkcraft.mod.common.magic.systems.symbolic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gnu.trove.map.hash.THashMap;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.mod.DarkcraftMod;
import net.minecraft.nbt.NBTTagCompound;

public class SymbolicRegistry
{
	private static List<ISymbolicFactory> factories = new ArrayList<ISymbolicFactory>();
	private static Map<String,ISymbolicFactory> idToFactoryMap = new THashMap();

	/**
	 * Attempt to register your factory
	 * @param factory the factory you wish to register
	 * @return true if registration succeeded, false if it didn't
	 */
	public static boolean registerFactory(ISymbolicFactory factory)
	{
		if(DarkcraftMod.inited) return false;
		factories.add(factory);
		return true;
	}

	public static String match(String glyphs)
	{
		for(ISymbolicFactory f : factories)
		{
			String s = f.match(glyphs);
			if(s != null)
			{
				idToFactoryMap.put(s, f);
				return s;
			}
		}
		return null;
	}

	private static ISymbolicFactory getFactory(String id, String glyphs)
	{
		if(!idToFactoryMap.containsKey(id))
		{
			String temp = match(glyphs);
			if(!glyphs.equals(temp)) return null;
		}
		return idToFactoryMap.get(id);
	}

	public static ISymbolicSpell createSpell(String id, String glyphs, SimpleCoordStore rootRune, SimpleCoordStore center)
	{
		ISymbolicFactory factory = getFactory(id,glyphs);
		if(factory == null) return null;
		return factory.createSpell(id, glyphs, rootRune, center);
	}

	public static ISymbolicSpell loadSpell(String id, String glyphs, NBTTagCompound nbt)
	{
		ISymbolicFactory factory = getFactory(id,glyphs);
		if(factory == null) return null;
		return factory.loadSpell(id, glyphs, nbt);
	}
}
