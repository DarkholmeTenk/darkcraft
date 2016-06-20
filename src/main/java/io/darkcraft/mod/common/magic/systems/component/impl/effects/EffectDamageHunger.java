package io.darkcraft.mod.common.magic.systems.component.impl.effects;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;

public class EffectDamageHunger extends AbstractDarkcraftEffect
{
	private static final UVStore uv = new UVStore(0,0.1,0.2,0.3);
	public EffectDamageHunger(ICaster _caster, EntityLivingBase ent, int _magnitude, int _duration)
	{
		super("damagehunger", _caster, ent, _magnitude, _duration-1, true, true, 20);
	}

	@Override
	public UVStore getIconLocation()
	{
		return uv;
	}

	@Override
	public void apply()
	{
		Entity e = getEntity();
		if(!(e instanceof EntityPlayer)) return;
		EntityPlayer pl = (EntityPlayer)e;
		FoodStats fs = pl.getFoodStats();
		if(fs.getSaturationLevel() > 0)
			fs.addStats(0,-magnitude);
		else
			fs.addStats(-magnitude,0);
	}

}
