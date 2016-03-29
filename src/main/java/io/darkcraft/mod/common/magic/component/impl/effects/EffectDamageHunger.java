package io.darkcraft.mod.common.magic.component.impl.effects;

import io.darkcraft.mod.common.magic.caster.ICaster;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;

public class EffectDamageHunger extends AbstractDarkcraftEffect
{
	public EffectDamageHunger(ICaster _caster, EntityLivingBase ent, int _magnitude, int _duration)
	{
		super("damagehunger", _caster, ent, _magnitude, _duration-1, true, true, 20);
	}

	@Override
	public void apply()
	{
		Entity e = getEntity();
		if(!(e instanceof EntityPlayer)) return;
		EntityPlayer pl = (EntityPlayer)e;
		FoodStats fs = pl.getFoodStats();
		if(fs.getSaturationLevel() > 0)
			fs.setFoodSaturationLevel(Math.max(fs.getSaturationLevel() - magnitude,0));
		else
			fs.setFoodLevel(Math.max(fs.getFoodLevel()-magnitude,0));
	}

}
