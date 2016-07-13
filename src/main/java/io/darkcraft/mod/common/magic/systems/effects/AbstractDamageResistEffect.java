package io.darkcraft.mod.common.magic.systems.effects;

import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

public abstract class AbstractDamageResistEffect extends AbstractDarkcraftEffect
{
	public AbstractDamageResistEffect(String _id, ICaster _caster, EntityLivingBase ent, int _magnitude, int _duration, boolean _visible, boolean _doesTick, int _tickFreq)
	{
		super(_id, _caster, ent, _magnitude, _duration, _visible, _doesTick, _tickFreq);
	}

	public float damageReduce(float oldDamage)
	{
		if((magnitude == 5) || (oldDamage <= (magnitude * 2))) return 0;
		return oldDamage - ((oldDamage * magnitude) / 5f);
	}

	public abstract float getNewDamage(DamageSource ds, float oldDamage);
}
