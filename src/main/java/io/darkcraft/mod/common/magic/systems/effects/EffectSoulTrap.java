package io.darkcraft.mod.common.magic.systems.effects;

import net.minecraft.entity.EntityLivingBase;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;

public class EffectSoulTrap extends AbstractDarkcraftEffect
{
	public static final UVStore uv = new UVStore(0.3,0.4,0.0,0.1);
	public EffectSoulTrap(ICaster _caster, EntityLivingBase ent, int _magnitude, int _duration)
	{
		super("soultrap", _caster, ent, _magnitude, _duration, true, false, 0);
	}

	@Override
	public void apply(){}

	@Override
	public UVStore getIconLocation(){ return uv; }
}
