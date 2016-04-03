package io.darkcraft.mod.common.magic.component.impl.effects;

import io.darkcraft.darkcore.mod.abstracts.effects.AbstractEffect;
import io.darkcraft.darkcore.mod.abstracts.effects.IEffectFactory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

public class DarkcraftEffectFactory implements IEffectFactory
{

	@Override
	public AbstractEffect createEffect(EntityLivingBase ent, String id, NBTTagCompound nbt)
	{
		if(id == null) return null;
		if(!id.startsWith("darkcraft.")) return null;
		int mag = nbt.getInteger("mag");
		int dur = nbt.getInteger("dur");
		if(id.equals("darkcraft.damage"))
			return new EffectDamage(ent, null, mag, dur);
		if(id.equals("darkcraft.fly"))
			return new EffectFly(null,ent,mag,dur);
		if(id.equals("darkcraft.damagehunger"))
			return new EffectDamageHunger(null,ent,mag,dur);
		if(id.equals("darkcraft.damagemagicka"))
			return new EffectDamageMagicka(null,ent,mag,dur);
		if(id.equals("darkcraft.restore"))
			return new EffectRestoreHealth(null,ent,mag,dur);
		if(id.equals("darkcraft.restorehunger"))
			return new EffectRestoreHunger(null,ent,mag,dur);
		if(id.equals("darkcraft.soultrap"))
			return new EffectSoulTrap(null,ent,mag,dur);
		return null;
	}

}
