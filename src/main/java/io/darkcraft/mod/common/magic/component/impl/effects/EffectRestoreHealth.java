package io.darkcraft.mod.common.magic.component.impl.effects;

import io.darkcraft.mod.common.magic.caster.ICaster;
import net.minecraft.entity.EntityLivingBase;

public class EffectRestoreHealth extends AbstractDarkcraftEffect
{
	public EffectRestoreHealth(ICaster _caster, EntityLivingBase ent, int magnitude, int duration)
	{
		super("restore", _caster, ent, magnitude, duration-1, true, true, 20);
	}

	@Override
	public void apply()
	{
		EntityLivingBase ent = getEntity();
		if(ent == null) return;
		ent.heal(magnitude);
	}

}
