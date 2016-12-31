package io.darkcraft.mod.common.magic.systems.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;

public class EffectRestoreHealth extends AbstractDarkcraftEffect
{
	public EffectRestoreHealth(ICaster _caster, Entity ent, int magnitude, int duration)
	{
		super("restore", _caster, ent, magnitude, duration-1, true, true, 20);
		canStack = true;
	}

	private static final UVStore uv = new UVStore(0.2,0.3,0,0.1);
	@Override
	public UVStore getIconLocation(){ return uv; }

	@Override
	public void apply()
	{
		EntityLivingBase ent = getEntityLB();
		if(ent == null) return;
		ent.heal(magnitude);
	}

}
