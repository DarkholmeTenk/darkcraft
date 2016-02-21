package io.darkcraft.mod.common.magic.component.impl.effects;

import io.darkcraft.mod.common.magic.caster.EntityCaster;
import io.darkcraft.mod.common.magic.caster.ICaster;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

public class EffectDamage extends AbstractDarkcraftEffect
{

	public EffectDamage(EntityLivingBase ent, ICaster caster, int magnitude, int duration)
	{
		super("damage", caster, ent, magnitude, duration, true, true, 20);
	}

	@Override
	public void apply()
	{
		entity.attackEntityFrom(DamageSource.magic, magnitude);
		System.out.println("Damage"+getTT()+"/"+duration);
		if(caster instanceof EntityCaster)
		{
			EntityLivingBase ent = ((EntityCaster) caster).getCaster();
			entity.setRevengeTarget(ent);
		}
	}

}
