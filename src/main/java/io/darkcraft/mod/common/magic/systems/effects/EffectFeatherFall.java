package io.darkcraft.mod.common.magic.systems.effects;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import net.minecraft.entity.EntityLivingBase;

public class EffectFeatherFall extends AbstractDarkcraftEffect
{
	public EffectFeatherFall(ICaster caster, EntityLivingBase ent, int magnitude, int duration)
	{
		super("featherfall", caster, ent, magnitude, duration-1, true, false, 20);
	}

	@Override
	public void apply()
	{
		// TODO Auto-generated method stub

	}

	private static final UVStore uv = new UVStore(0.1,0.2,0.2,0.3);
	@Override
	public UVStore getIconLocation()
	{
		return uv;
	}
}
