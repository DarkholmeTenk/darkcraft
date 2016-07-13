package io.darkcraft.mod.common.registries;

import io.darkcraft.darkcore.mod.handlers.EffectHandler;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.MagicEventHandler;
import io.darkcraft.mod.common.magic.systems.component.SpellPartRegistry;
import io.darkcraft.mod.common.magic.systems.component.impl.Blink;
import io.darkcraft.mod.common.magic.systems.component.impl.Damage;
import io.darkcraft.mod.common.magic.systems.component.impl.DamageFire;
import io.darkcraft.mod.common.magic.systems.component.impl.DamageHunger;
import io.darkcraft.mod.common.magic.systems.component.impl.DamageMagicka;
import io.darkcraft.mod.common.magic.systems.component.impl.Dig;
import io.darkcraft.mod.common.magic.systems.component.impl.DigFortune;
import io.darkcraft.mod.common.magic.systems.component.impl.DigSilk;
import io.darkcraft.mod.common.magic.systems.component.impl.DigVein;
import io.darkcraft.mod.common.magic.systems.component.impl.FeatherFall;
import io.darkcraft.mod.common.magic.systems.component.impl.Fly;
import io.darkcraft.mod.common.magic.systems.component.impl.Mark;
import io.darkcraft.mod.common.magic.systems.component.impl.Recall;
import io.darkcraft.mod.common.magic.systems.component.impl.ResistFire;
import io.darkcraft.mod.common.magic.systems.component.impl.RestoreHealth;
import io.darkcraft.mod.common.magic.systems.component.impl.RestoreHunger;
import io.darkcraft.mod.common.magic.systems.component.impl.SoulTrap;
import io.darkcraft.mod.common.magic.systems.component.impl.SummonLight;
import io.darkcraft.mod.common.magic.systems.component.impl.Test;
import io.darkcraft.mod.common.magic.systems.component.impl.WaterWalk;
import io.darkcraft.mod.common.magic.systems.effects.DarkcraftEffectFactory;
import io.darkcraft.mod.common.magic.systems.soulspell.SoulSpellRegistry;
import io.darkcraft.mod.common.magic.systems.soulspell.impl.DarkcraftSoulSpellFactory;
import io.darkcraft.mod.common.magic.systems.spell.Spell;
import io.darkcraft.mod.common.magic.systems.symbolic.SymbolicRegistry;
import io.darkcraft.mod.common.magic.systems.symbolic.impl.DarkcraftSymbolicFactory;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class MagicalRegistry
{
	public static final ResourceLocation componentTex = new ResourceLocation(DarkcraftMod.modName,"textures/gui/components/components.png");
	public static final ResourceLocation projectileTex = new ResourceLocation(DarkcraftMod.modName,"textures/project/projectiles.png");
	public static final DamageSource magicDamage = new DamageSource("dcMagicDamage").setMagicDamage();

	public static final Dig dig = new Dig();
	public static final DigFortune digfortune = new DigFortune();
	public static final DigSilk digsilk = new DigSilk();
	public static final DigVein digvein = new DigVein();
	public static final Damage damage = new Damage();
	public static final DamageHunger damageHunger = new DamageHunger();
	public static final DamageMagicka damageMagicka = new DamageMagicka();
	public static final RestoreHealth restoreHealth = new RestoreHealth();
	public static final RestoreHunger restoreHunger = new RestoreHunger();
	public static final Fly fly = new Fly();
	public static final SoulTrap soulTrap = new SoulTrap();
	public static final Mark mark = new Mark();
	public static final Recall recall = new Recall(false);
	public static final Recall recallCD = new Recall(true);
	public static final SummonLight sl = new SummonLight();
	public static final Blink blink = new Blink();
	public static final WaterWalk ww = new WaterWalk();
	public static final FeatherFall ff = new FeatherFall();
	public static final DamageFire df = new DamageFire();
	public static final ResistFire rf = new ResistFire();
	public static final Test test = new Test();

	public static void registerMagic()
	{
		SpellPartRegistry.registerComponent(dig);
		SpellPartRegistry.registerComponent(digsilk);
		SpellPartRegistry.registerComponent(digfortune);
		SpellPartRegistry.registerComponent(digvein);
		SpellPartRegistry.registerComponent(damage);
		SpellPartRegistry.registerComponent(damageHunger);
		SpellPartRegistry.registerComponent(damageMagicka);
		SpellPartRegistry.registerComponent(restoreHealth);
		SpellPartRegistry.registerComponent(restoreHunger);
		SpellPartRegistry.registerComponent(fly);
		SpellPartRegistry.registerComponent(soulTrap);
		SpellPartRegistry.registerComponent(mark);
		SpellPartRegistry.registerComponent(recall);
		SpellPartRegistry.registerComponent(recallCD);
		SpellPartRegistry.registerComponent(sl);
		SpellPartRegistry.registerComponent(blink);
		SpellPartRegistry.registerComponent(ww);
		SpellPartRegistry.registerComponent(ff);
		SpellPartRegistry.registerComponent(df);
		SpellPartRegistry.registerComponent(rf);
		SpellPartRegistry.registerComponent(test, false);
		SoulSpellRegistry.registerFactory(new DarkcraftSoulSpellFactory());
		SymbolicRegistry.registerFactory(new DarkcraftSymbolicFactory());
		EffectHandler.registerEffectFactory(new DarkcraftEffectFactory());

		new MagicEventHandler();

		magicDamage.setDamageBypassesArmor().setMagicDamage();
	}

	public static Spell getPrefabSpell(String id)
	{
		return null;
	}
}
