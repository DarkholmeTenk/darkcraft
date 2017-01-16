package io.darkcraft.mod.common.magic.systems.symbolic.type;

public abstract class SymbolicEffect extends BaseSymbolic
{
	/**
	 * Will only be called when placed on a valid tile
	 */
	public abstract void tick();

	public static abstract class SymbolicEntityEffect extends SymbolicEffect
	{
		@Override
		public boolean isValidSymbolic()
		{
			return getSelector() != null;
		}

		@Override
		public boolean setSelector(Selector selector)
		{
			if(!selector.selectEntities())
				return false;
			return super.setSelector(selector);
		}
	}

	public static abstract class SymbolicBlockEffect extends SymbolicEffect
	{
		@Override
		public boolean isValidSymbolic()
		{
			return getSelector() != null;
		}

		@Override
		public boolean setSelector(Selector selector)
		{
			if(!selector.selectBlocks())
				return false;
			return super.setSelector(selector);
		}
	}
}