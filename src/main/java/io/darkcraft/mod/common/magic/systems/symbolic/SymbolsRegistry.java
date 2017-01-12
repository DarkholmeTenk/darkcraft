package io.darkcraft.mod.common.magic.systems.symbolic;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import io.darkcraft.mod.common.magic.systems.symbolic.type.BaseSymbolic;

public class SymbolsRegistry
{
	private Map<String, Constructor<? extends BaseSymbolic>> symbolMap = new HashMap<>();

	public void register(Class<? extends BaseSymbolic>... classes)
	{
		for(Class<? extends BaseSymbolic> clazz : classes)
		{
			try
			{
				Constructor<? extends BaseSymbolic> constructor = clazz.getConstructor();
			}
			catch (NoSuchMethodException | SecurityException e)
			{
				throw new RuntimeException("Unable to register class " + clazz.getName(), e);
			}
		}
	}
}
