package io.darkcraft.darkcraft.mod.common.spellsystem.components.modifiers;

import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellComponent;

public class DamageMod extends AAbstractModifier
{

	@Override
	public double getCostCoefficient(int exponent)
	{
		if(exponent == 1)
			return getStrength();
		return 0;
	}

	@Override
	public String getID()
	{
		return "dammod";
	}

	@Override
	public ISpellComponent create()
	{
		return new DamageMod();
	}

}
