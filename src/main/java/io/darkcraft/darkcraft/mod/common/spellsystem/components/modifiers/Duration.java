package io.darkcraft.darkcraft.mod.common.spellsystem.components.modifiers;

import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellComponent;

public class Duration extends AAbstractModifier
{

	@Override
	public String getID()
	{
		return "dur";
	}

	@Override
	public ISpellComponent create()
	{
		return new Duration();
	}

	@Override
	public double getCostCoefficient(int exponent)
	{
		if(exponent == 1)
			return getStrength();
		return 0;
	}

}
