package io.darkcraft.darkcraft.mod.common.spellsystem.components.shapes;

import java.util.Set;

import io.darkcraft.darkcraft.mod.common.spellsystem.components.modifiers.Duration;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellComponent;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellModifier;

public class Zone extends Area
{
	private int durMod = 1;
	
	@Override
	public String getID()
	{
		return "zone";
	}
	
	@Override
	public ISpellComponent create()
	{
		System.out.println("NZ!");
		return new Zone();
	}
	
	@Override
	public double getCostCoefficient(int exponent)
	{
		switch(exponent)
		{
			case 2 : return 0.75;
			case 1 : return 0.5;
			default : return 0;
		}
	}
	
	@Override
	public int getDuration()
	{
		return 20 * durMod;
	}
	
	@Override
	public void applyModifiers(Set<ISpellModifier> modifiers)
	{
		for(ISpellModifier m : modifiers)
		{
			System.out.println("Adding modifier " + m.getID());
			if(m instanceof Duration)
				durMod = 1+m.getStrength();
		}
	}
}
