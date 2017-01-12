package io.darkcraft.mod.common.magic.systems.symbolic.type;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;

public abstract class Selector extends BaseSymbolic
{
	@Override
	public boolean setSelector(Selector selector)
	{
		return false;
	}

	public List<SimpleCoordStore> getAffectedBlocks()
	{
		return new ArrayList<>();
	}

	public abstract boolean selectBlocks();

	public List<EntityLivingBase> getAffectedEntities()
	{
		return new ArrayList<>();
	}

	public abstract boolean selectEntities();

	@Override
	public boolean isValidSymbolic()
	{
		return false;
	}
}
