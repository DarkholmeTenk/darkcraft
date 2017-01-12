package io.darkcraft.mod.common.magic.systems.symbolic.type;

public abstract class SymbolicEffect extends BaseSymbolic
{

	public static abstract class SymbolicEntityEffect extends BaseSymbolic
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

	public static abstract class SymbolicBlockEffect extends BaseSymbolic
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