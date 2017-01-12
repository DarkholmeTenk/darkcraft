package io.darkcraft.mod.common.magic.systems.symbolic.type;

import io.darkcraft.darkcore.mod.abstracts.AbstractTileEntity;

public abstract class BaseSymbolic
{
	/**
	 * @return the id of the symbol. This is also the text which makes up the symbol.
	 */
	public abstract String id();

	/**
	 * @return true if this is a symbol in a state that can be drawn
	 */
	public boolean isValidSymbolic()
	{
		return false;
	}

	/**
	 * Will only be called when placed on a valid tile
	 */
	public abstract void tick();

	/**
	 * Used to add a modifier to the symbol
	 * @param modifier the modifier to add
	 * @return true if the modifier is valid here (or doesn't invalidate at least), false if otherwise
	 */
	public boolean addModifier(Modifier modifier)
	{
		return false;
	}

	private Selector selector;
	public boolean setSelector(Selector selector)
	{
		if(this.selector == null)
		{
			this.selector = selector;
			return true;
		}
		else
			return false;
	}

	public Selector getSelector()
	{
		return selector;
	}

	private AbstractTileEntity tileEntity;
	public void setTileEntity(AbstractTileEntity tileEntity)
	{
		if(this.tileEntity == null)
			this.tileEntity = tileEntity;
	}

	public AbstractTileEntity getTileEntity()
	{
		return tileEntity;
	}
}
