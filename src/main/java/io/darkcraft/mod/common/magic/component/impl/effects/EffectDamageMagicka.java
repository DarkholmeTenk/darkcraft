package io.darkcraft.mod.common.magic.component.impl.effects;

import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.caster.EntityCaster;
import io.darkcraft.mod.common.magic.caster.ICaster;
import net.minecraft.entity.EntityLivingBase;

public class EffectDamageMagicka extends AbstractDarkcraftEffect
{
	public EffectDamageMagicka(ICaster _caster, EntityLivingBase ent, int _magnitude, int _duration)
	{
		super("damagemagicka", _caster, ent, _magnitude, _duration-1, true, true, 20);
	}

	@Override
	public void apply()
	{
		EntityLivingBase ent = getEntity();
		if(ent == null) return;
		EntityCaster ec = Helper.getCaster(ent);
		ec.useMana(magnitude, false);
	}

}
