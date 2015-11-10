package io.darkcraft.mod.common.magic.caster;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.mod.common.entities.EntitySpellProjectile;

public class BlockCaster implements ICaster
{
	public final SimpleCoordStore blockPos;

	public BlockCaster(SimpleCoordStore pos)
	{
		blockPos = pos;
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

	@Override
	public SimpleDoubleCoordStore getSpellCreationPos()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setVelocity(EntitySpellProjectile sp)
	{
		// TODO Auto-generated method stub

	}
}
