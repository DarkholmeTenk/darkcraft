package io.darkcraft.mod.common.magic.caster;

import java.lang.ref.WeakReference;

import net.minecraft.entity.EntityLivingBase;

public class EntityCaster implements ICaster
{
	private final WeakReference<EntityLivingBase> caster;

	public EntityCaster(EntityLivingBase ent)
	{
		caster = new WeakReference(ent);
	}

	public EntityLivingBase getCaster()
	{
		return caster.get();
	}

	@Override
	public double getMana()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMaxMana()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean useMana(double amount, boolean sim)
	{
		return true;
	}
}
