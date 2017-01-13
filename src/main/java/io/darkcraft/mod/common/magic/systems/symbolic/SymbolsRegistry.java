package io.darkcraft.mod.common.magic.systems.symbolic;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import io.darkcraft.mod.common.magic.systems.symbolic.type.BaseSymbolic;
import io.darkcraft.mod.common.magic.systems.symbolic.type.Modifier;
import io.darkcraft.mod.common.magic.systems.symbolic.type.Selector;
import io.darkcraft.mod.common.magic.systems.symbolic.type.SymbolicEffect;

public class SymbolsRegistry
{
	private static Map<String, WrappedConstructor<? extends BaseSymbolic>> symbolMap = new HashMap<>();

	public static void register(Class<? extends BaseSymbolic>... classes)
	{
		for(Class<? extends BaseSymbolic> clazz : classes)
		{
			try
			{
				Constructor<? extends BaseSymbolic> constructor = clazz.getConstructor();
				BaseSymbolic first = constructor.newInstance();
				String id = first.id();
				if(symbolMap.containsKey(id))
					throw new RuntimeException("Class " + clazz.getName() + " conflicts with " + symbolMap.get(id).getDeclaringClass().getName());
				symbolMap.put(id, new WrappedConstructor(constructor));
			}
			catch (Exception e)
			{
				throw new RuntimeException("Unable to register class " + clazz.getName(), e);
			}
		}
	}

	public static BaseSymbolic getSymbol(String id)
	{
		WrappedConstructor<? extends BaseSymbolic> constructor = symbolMap.get(id);
		if(constructor == null)
			return null;
		return constructor.construct();
	}

	public static SymbolicEffect getFinalSymbol(String[] words)
	{
		List<Modifier> currentModifiers = Lists.newArrayList();
		Selector selector = null;
		SymbolicEffect effect = null;
		for(String word : words)
		{
			BaseSymbolic symbol = SymbolsRegistry.getSymbol(word);
			if(symbol == null)
				return new DeadEffect("Unrecognised symbol: " + word);
			if(effect != null)
				return new DeadEffect("Cannot have multiple effects");
			if(symbol instanceof Modifier)
				currentModifiers.add((Modifier) symbol);
			else
			{
				for(Modifier m : currentModifiers)
					if(!symbol.addModifier(m))
						return new DeadEffect(word + " cannot take " + m.id() + " as modifier");
				if(selector != null)
					if(!symbol.setSelector(selector))
						return new DeadEffect(word + " cannot take " + selector.id() + " as selector");
				if(symbol instanceof Selector)
					selector = (Selector) symbol;
				else if(symbol instanceof SymbolicEffect)
					effect = (SymbolicEffect) symbol;
			}
		}
		if(effect == null)
			return new DeadEffect("No effect detected");
		if(!effect.isValidSymbolic())
			return new DeadEffect("Symbol is invalid");
		return effect;
	}

	public static class WrappedConstructor<T>
	{
		private final Constructor<T> constructor;
		public WrappedConstructor(Constructor<T> t)
		{
			this.constructor = t;
		}

		public T construct(Object... args)
		{
			try
			{
				return constructor.newInstance(args);
			}
			catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				e.printStackTrace();
				return null;
			}
		}

		public Class getDeclaringClass()
		{
			return constructor.getDeclaringClass();
		}
	}
}
