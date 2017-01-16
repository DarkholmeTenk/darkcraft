package io.darkcraft.mod.common.magic.systems.symbolic.type;

import java.util.ArrayList;
import java.util.List;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.mod.common.magic.blocks.tileent.MagicRune;
import net.minecraft.entity.EntityLivingBase;

public abstract class Selector extends BaseSymbolic
{
	@Override
	public boolean setSelector(Selector selector)
	{
		return false;
	}

	public List<SimpleCoordStore> getAffectedBlocks(MagicRune rune)
	{
		return new ArrayList<>();
	}

	public abstract boolean selectBlocks();

	public List<EntityLivingBase> getAffectedEntities(MagicRune rune)
	{
		return new ArrayList<>();
	}

	public abstract boolean selectEntities();

	@Override
	public boolean isValidSymbolic()
	{
		return false;
	}
	
	public static abstract class EntitySelector extends Selector
	{
		@Override
		public boolean selectBlocks()
		{
			return false;
		}
		
		@Override
		public boolean selectEntities()
		{
			return true;
		}

		@Override
		public abstract List<EntityLivingBase> getAffectedEntities(MagicRune rune);
	}
	
	public static abstract class BlockSelector extends Selector
	{
		@Override
		public boolean selectBlocks()
		{
			return true;
		}
		
		@Override
		public boolean selectEntities()
		{
			return false;
		}
		
		@Override
		public abstract List<SimpleCoordStore> getAffectedBlocks(MagicRune rune);
	}
}
