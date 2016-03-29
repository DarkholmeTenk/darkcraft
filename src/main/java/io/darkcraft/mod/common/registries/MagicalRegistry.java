package io.darkcraft.mod.common.registries;

import io.darkcraft.darkcore.mod.handlers.EffectHandler;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.MagicEventHandler;
import io.darkcraft.mod.common.magic.SpellPartRegistry;
import io.darkcraft.mod.common.magic.component.impl.Damage;
import io.darkcraft.mod.common.magic.component.impl.Dig;
import io.darkcraft.mod.common.magic.component.impl.Fly;
import io.darkcraft.mod.common.magic.component.impl.Test;
import io.darkcraft.mod.common.magic.component.impl.effects.DarkcraftEffectFactory;
import io.darkcraft.mod.common.magic.spell.Spell;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class MagicalRegistry
{
	public static final ResourceLocation componentTex = new ResourceLocation(DarkcraftMod.modName,"textures/gui/components/components.png");
	public static final ResourceLocation projectileTex = new ResourceLocation(DarkcraftMod.modName,"textures/project/projectiles.png");
	public static final DamageSource magicDamage = new DamageSource("dcMagicDamage");
	public static final Dig dig = new Dig();
	public static final Damage damage = new Damage();
	public static final Fly fly = new Fly();
	public static final Test test = new Test();
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
