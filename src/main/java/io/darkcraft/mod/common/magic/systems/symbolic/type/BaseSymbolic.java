package io.darkcraft.mod.common.magic.systems.symbolic.type;

import io.darkcraft.mod.common.magic.blocks.tileent.MagicRune;
import net.minecraft.nbt.NBTTagCompound;

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

	private MagicRune tileEntity;
	public void setTileEntity(MagicRune tileEntity)
	{
		if(this.tileEntity == null)
			this.tileEntity = tileEntity;
	}

	public MagicRune getTileEntity()
	{
		return tileEntity;
	}

	public void writeToNBT(NBTTagCompound nbt){}

	public void readFromNBT(NBTTagCompound nbt){}
}
