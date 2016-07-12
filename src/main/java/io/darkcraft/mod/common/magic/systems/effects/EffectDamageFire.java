package io.darkcraft.mod.common.magic.systems.effects;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.common.magic.systems.spell.caster.EntityCaster;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

public class EffectDamageFire extends EffectDamage
{
	private static final UVStore uv = new UVStore(0,0.1,0.4,0.5);

	public EffectDamageFire(ICaster caster, EntityLivingBase ent, int magnitude, int duration)
	{
		super("damagefire", caster, ent, magnitude, duration);
	}

	@Override
	public UVStore getIconLocation()
	{
		return uv;
	}

	@Override
	public DamageSource getDS(ICaster caster)
	{
		if(caster instanceof EntityCaster)
		{
			EntityLivingBase ecaster = ((EntityCaster) caster).getCaster();
			return new EntityDamageSource("fireball",ecaster).setFireDamage();
		}
		return DamageSource.onFire;
	}

	@Override
	public void apply()
	{
		EntityLivingBase toAttack = getEntity();
		toAttack.attackEntityFrom(getDS(caster), magnitude);
		toAttack.setFire(2);
	}

}
