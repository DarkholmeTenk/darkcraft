package io.darkcraft.darkcraft.mod.common.spellsystem.components.modifiers;

import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellComponent;

public class Size extends AAbstractModifier
{

	@Override
	public String getID()
	{
		return "siz";
	}

	@Override
	public ISpellComponent create()
	{
		return new Size();
	}

	@Override
	public double getCostCoefficient(int exponent)
	{
		if(exponent == 2)
			return getStrength();
		return 0;
	}

}
