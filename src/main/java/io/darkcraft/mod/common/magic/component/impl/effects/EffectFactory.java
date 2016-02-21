package io.darkcraft.mod.common.magic.component.impl.effects;

import io.darkcraft.darkcore.mod.abstracts.effects.AbstractEffect;
import io.darkcraft.darkcore.mod.abstracts.effects.IEffectFactory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

public class EffectFactory implements IEffectFactory
{

	@Override
	public AbstractEffect createEffect(EntityLivingBase ent, String id, NBTTagCompound nbt)
	{
		if(id == null) return null;
		int mag = nbt.getInteger("mag");
		int dur = nbt.getInteger("dur");
		if(id.equals("darkcraft.damage"))
			return new EffectDamage(ent, null, mag, dur);
		return null;
	}

}
