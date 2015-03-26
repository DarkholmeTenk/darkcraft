package io.darkcraft.darkcraft.mod.common.spellsystem.components.modifiers;

import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellModifier;

public abstract class AAbstractModifier implements ISpellModifier
{
	protected int	strength	= 1;

	@Override
	public void setStrength(int _strength)
	{
		strength = _strength;
	}

	@Override
	public int getStrength()
	{
		return strength;
	}
	
	@Override
	public int hashCode()
	{
		return getID().hashCode();
	}
	
	public boolean equals(Object o)
	{
		if(o == this)
			return true;
		if(!(o instanceof AAbstractModifier))
			return false;
		return ((AAbstractModifier)o).getID().equals(this.getID());
	}
}
