package io.darkcraft.mod.common.registries;

import io.darkcraft.mod.common.magic.SpellPartRegistry;
import io.darkcraft.mod.common.magic.component.impl.Damage;

public class MagicalRegistry
{
	private static Damage damage = new Damage();

	public static void registerMagic()
	{
		SpellPartRegistry.registerComponent(damage);
	}
}
