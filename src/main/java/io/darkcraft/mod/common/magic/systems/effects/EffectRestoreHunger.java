package io.darkcraft.mod.common.magic.systems.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;

public class EffectRestoreHunger extends AbstractDarkcraftEffect
{
	public EffectRestoreHunger(ICaster _caster, Entity ent, int magnitude, int duration)
	{
		super("restorehunger", _caster, ent, magnitude, duration-1, true, true, 20);
		canStack = true;
	}

	private static final UVStore uv = new UVStore(0.2,0.3,0.1,0.2);
	@Override
	public UVStore getIconLocation(){ return uv; }

	@Override
	public void apply()
	{
		Entity ent = getEntity();
		if(!(ent instanceof EntityPlayer)) return;
		FoodStats fs = ((EntityPlayer)ent).getFoodStats();
		if(fs.needFood())
			fs.addStats(magnitude, 1);
		else
			fs.addStats(1, magnitude);
	}

}
