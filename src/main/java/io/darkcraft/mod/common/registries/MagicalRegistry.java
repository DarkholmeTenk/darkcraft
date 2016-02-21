package io.darkcraft.mod.common.registries;

import io.darkcraft.darkcore.mod.handlers.EffectHandler;
import io.darkcraft.mod.common.magic.SpellPartRegistry;
import io.darkcraft.mod.common.magic.component.impl.Damage;
import io.darkcraft.mod.common.magic.component.impl.Dig;
import io.darkcraft.mod.common.magic.component.impl.Test;
import io.darkcraft.mod.common.magic.component.impl.effects.EffectFactory;
import io.darkcraft.mod.common.magic.spell.Spell;

public class MagicalRegistry
{
	private static Dig dig = new Dig();
	private static Damage damage = new Damage();
	private static Test test = new Test();

	public static void registerMagic()
	{
		SpellPartRegistry.registerComponent(dig);
		SpellPartRegistry.registerComponent(damage);
		SpellPartRegistry.registerComponent(test);
		EffectHandler.registerEffectFactory(new EffectFactory());
	}

	public static Spell getPrefabSpell(String id)
	{
		return null;
	}
}
