package io.darkcraft.mod.common.magic.systems.effects;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.systems.spell.caster.EntityCaster;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import net.minecraft.entity.EntityLivingBase;

public class EffectDamageMagicka extends AbstractDarkcraftEffect
{
	private static final UVStore uv = new UVStore(0,0.1,0.3,0.4);
	public EffectDamageMagicka(ICaster _caster, EntityLivingBase ent, int _magnitude, int _duration)
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
		EntityLivingBase ent = getEntity();
		if(ent == null) return;
		EntityCaster ec = Helper.getCaster(ent);
		ec.useMana(magnitude, false);
	}

}
