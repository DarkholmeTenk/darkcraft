package io.darkcraft.mod.common.registries;

import io.darkcraft.darkcore.mod.handlers.EffectHandler;
import io.darkcraft.mod.common.magic.MagicEventHandler;
import io.darkcraft.mod.common.magic.SpellPartRegistry;
import io.darkcraft.mod.common.magic.component.impl.Damage;
import io.darkcraft.mod.common.magic.component.impl.Dig;
import io.darkcraft.mod.common.magic.component.impl.Fly;
import io.darkcraft.mod.common.magic.component.impl.Test;
import io.darkcraft.mod.common.magic.component.impl.effects.DarkcraftEffectFactory;
import io.darkcraft.mod.common.magic.spell.Spell;
import net.minecraft.util.DamageSource;

public class MagicalRegistry
{
	public static final DamageSource magicDamage = new DamageSource("dcMagicDamage");
	private static Dig dig = new Dig();
	private static Damage damage = new Damage();
	private static Fly fly = new Fly();
	private static Test test = new Test();

	public static void registerMagic()
	{
		SpellPartRegistry.registerComponent(dig);
		SpellPartRegistry.registerComponent(damage);
		SpellPartRegistry.registerComponent(fly);
		SpellPartRegistry.registerComponent(test);
		EffectHandler.registerEffectFactory(new DarkcraftEffectFactory());

		new MagicEventHandler();

		magicDamage.setDamageBypassesArmor().setMagicDamage();
	}

	public static Spell getPrefabSpell(String id)
	{
		return null;
	}
}
