package io.darkcraft.mod.common.magic.component.impl.effects;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.common.magic.caster.ICaster;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;

public class EffectRestoreHunger extends AbstractDarkcraftEffect
{
	public EffectRestoreHunger(ICaster _caster, EntityLivingBase ent, int magnitude, int duration)
	{
		super("restorehunger", _caster, ent, magnitude, duration-1, true, true, 20);
	}

	private static final UVStore uv = new UVStore(0.2,0.3,0.1,0.2);
	@Override
	public UVStore getIconLocation(){ return uv; }

	@Override
	public void apply()
	{
		EntityLivingBase ent = getEntity();
		if(!(ent instanceof EntityPlayer)) return;
		FoodStats fs = ((EntityPlayer)ent).getFoodStats();
		if(fs.needFood())
			fs.setFoodLevel(Math.min(20,fs.getFoodLevel()+magnitude));
		else
			fs.setFoodSaturationLevel(Math.min(20, fs.getSaturationLevel()+magnitude));
	}

}
