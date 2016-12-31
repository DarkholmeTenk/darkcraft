package io.darkcraft.mod.common.magic.systems.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.common.magic.systems.spell.caster.EntityCaster;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import io.darkcraft.mod.common.registries.MagicalRegistry;

public class EffectDamage extends AbstractDarkcraftEffect
{
	private static final UVStore lqUV = new UVStore(0,0.1,0,0.1);
	private static final UVStore hqUV = new UVStore(0,0.1,0.1,0.2);

	public EffectDamage(ICaster caster, Entity ent, int magnitude, int duration)
	{
		super("damage", caster, ent, magnitude, duration-1, true, true, 20);
		canStack = true;
	}

	protected EffectDamage(String id, ICaster caster, Entity ent, int magnitude, int duration)
	{
		super(id, caster, ent, magnitude, duration-1, true, true, 20);
	}

	@Override
	public UVStore getIconLocation()
	{
		if(magnitude > 20)
			return hqUV;
		return lqUV;
	}

	public DamageSource getDS(ICaster caster)
	{
		if(caster instanceof EntityCaster)
		{
			EntityLivingBase ecaster = ((EntityCaster) caster).getCaster();
			return new EntityDamageSource(MagicalRegistry.magicDamage.damageType,ecaster).setMagicDamage();
		}
		return MagicalRegistry.magicDamage;
	}

	@Override
	public void apply()
	{
		Entity toAttack = getEntity();
		toAttack.attackEntityFrom(getDS(caster), magnitude);
	}
}
