package io.darkcraft.mod.common.magic.systems.effects;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

import io.darkcraft.darkcore.mod.abstracts.effects.AbstractEffect;
import io.darkcraft.darkcore.mod.abstracts.effects.IEffectFactory;

public class DarkcraftEffectFactory implements IEffectFactory
{

	@Override
	public AbstractEffect createEffect(Entity ent, String id, NBTTagCompound nbt)
	{
		if(id == null) return null;
		if(!id.startsWith("darkcraft.")) return null;
		int mag = nbt.getInteger("mag");
		int dur = nbt.getInteger("dur");
		if(id.equals("darkcraft.damage"))
			return new EffectDamage(null, ent, mag, dur);
		if(id.equals("darkcraft.damagefire"))
			return new EffectDamageFire(null, ent, mag, dur);
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
		if(id.equals("darkcraft.waterwalk"))
			return new EffectWaterWalk(null,ent,mag,dur);
		if(id.equals("darkcraft.ssmanaregen"))
			return new SSEffectManaRegen(null,ent,mag,dur);
		if(id.equals("darkcraft.featherfall"))
			return new EffectFeatherFall(null,ent,mag,dur);
		if(id.equals("darkcraft.resistfire"))
			return new EffectResistFire(null,ent,mag,dur);
		return null;
	}

}
