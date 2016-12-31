package io.darkcraft.mod.common.magic.systems.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;

public class EffectFly extends AbstractDamageResistEffect
{
	private final static UVStore flyUV = new UVStore(0.1,0.2,0,0.1);

	public EffectFly(ICaster _caster, Entity ent, int _magnitude, int _duration)
	{
		super("fly", _caster, ent, _magnitude, _duration, true, false, 0);
	}

	@Override
	public UVStore getIconLocation()
	{
		return flyUV;
	}

	@Override
	public void effectAdded()
	{
		Entity ent = getEntity();
		if((ent == null) || !(ent instanceof EntityPlayer)) return;
		EntityPlayer pl = (EntityPlayer) ent;
		pl.capabilities.allowFlying = true;
	}

	@Override
	public void effectRemoved()
	{
		Entity ent = getEntity();
		if((ent == null) || !(ent instanceof EntityPlayer)) return;
		EntityPlayer pl = (EntityPlayer) ent;
		if(pl.capabilities.isCreativeMode) return;
		pl.capabilities.allowFlying = false;
		pl.capabilities.isFlying = false;
	}

	@Override
	public void apply(){}

	@Override
	public float getNewDamage(DamageSource ds, float oldDamage)
	{
		return ds == DamageSource.fall ? 0 : oldDamage;
	}

}
