package io.darkcraft.mod.common.magic.systems.effects;

import net.minecraft.entity.Entity;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;

public class SSEffectManaRegen extends AbstractDarkcraftEffect
{
	public SSEffectManaRegen(ICaster _caster, Entity ent, int magnitude, int duration)
	{
		super("ssmanaregen", _caster, ent, magnitude, duration-1, true, false, 20);
	}

	private static final UVStore uv = new UVStore(0.6,0.7,0.0,0.1);
	@Override
	public UVStore getIconLocation(){ return uv; }

	@Override
	public void apply(){}
}
