package io.darkcraft.mod.common.magic.systems.effects;

import net.minecraft.entity.EntityLivingBase;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;

public class EffectSoulTrap extends AbstractDarkcraftEffect
{
	public static final UVStore uv = new UVStore(0.3,0.4,0.0,0.1);
	public EffectSoulTrap(ICaster _caster, EntityLivingBase ent, int _magnitude, int _duration)
	{
		super("soultrap", _caster, ent, _magnitude, _duration, true, true, 4);
	}

	@Override
	public void apply()
	{
		if(ServerHelper.isClient())
		{
			DarkcraftMod.particle.createSoulTrapParticles(container);
		}
	}

	@Override
	public UVStore getIconLocation(){ return uv; }
}
