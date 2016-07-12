package io.darkcraft.mod.common.magic.systems.effects;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import net.minecraft.entity.EntityLivingBase;

public class EffectRestoreHealth extends AbstractDarkcraftEffect
{
	public EffectRestoreHealth(ICaster _caster, EntityLivingBase ent, int magnitude, int duration)
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
		EntityLivingBase ent = getEntity();
		if(ent == null) return;
		ent.heal(magnitude);
	}

}
