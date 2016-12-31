package io.darkcraft.mod.common.magic.systems.effects;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;

public class EffectFeatherFall extends AbstractDamageResistEffect
{
	public EffectFeatherFall(ICaster caster, Entity ent, int magnitude, int duration)
	{
		super("featherfall", caster, ent, magnitude, duration-1, true, false, 20);
	}

	@Override
	public void apply()
	{
	}

	private static final UVStore uv = new UVStore(0.1,0.2,0.2,0.3);
	@Override
	public UVStore getIconLocation()
	{
		return uv;
	}

	@Override
	public float getNewDamage(DamageSource ds, float oldDamage)
	{
		if(ds != DamageSource.fall) return oldDamage;
		return damageReduce(oldDamage);
	}
}
