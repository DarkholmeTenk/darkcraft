package io.darkcraft.mod.common.magic.systems.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.systems.spell.caster.EntityCaster;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;

public class EffectDamageMagicka extends AbstractDarkcraftEffect
{
	private static final UVStore uv = new UVStore(0,0.1,0.3,0.4);
	public EffectDamageMagicka(ICaster _caster, Entity ent, int _magnitude, int _duration)
	{
		super("damagemagicka", _caster, ent, _magnitude, _duration-1, true, true, 20);
	}

	@Override
	public UVStore getIconLocation()
	{
		return uv;
	}

	@Override
	public void apply()
	{
		Entity ent = getEntity();
		if((ent == null) || !(ent instanceof EntityLivingBase)) return;
		EntityCaster ec = Helper.getCaster((EntityLivingBase) ent);
		ec.useMana(magnitude, false);
	}

}
