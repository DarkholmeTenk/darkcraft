package io.darkcraft.mod.common.magic.systems.component.impl.effects;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class EffectFly extends AbstractDarkcraftEffect
{
	private final static UVStore flyUV = new UVStore(0.1,0.2,0,0.1);

	public EffectFly(ICaster _caster, EntityLivingBase ent, int _magnitude, int _duration)
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
		EntityLivingBase ent = getEntity();
		if((ent == null) || !(ent instanceof EntityPlayer)) return;
		EntityPlayer pl = (EntityPlayer) ent;
		pl.capabilities.allowFlying = true;
	}

	@Override
	public void effectRemoved()
	{
		EntityLivingBase ent = getEntity();
		if((ent == null) || !(ent instanceof EntityPlayer)) return;
		EntityPlayer pl = (EntityPlayer) ent;
		pl.capabilities.allowFlying = false;
		pl.capabilities.isFlying = false;
	}

	@Override
	public void apply(){}

}
