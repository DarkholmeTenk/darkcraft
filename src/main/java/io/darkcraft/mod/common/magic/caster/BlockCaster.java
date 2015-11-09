package io.darkcraft.mod.common.magic.caster;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;

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
}
