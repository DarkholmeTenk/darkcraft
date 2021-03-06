package io.darkcraft.mod.common.magic.event.spell;

import io.darkcraft.mod.common.magic.systems.spell.Spell;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import cpw.mods.fml.common.eventhandler.Cancelable;

@Cancelable
public class SpellPreCastEvent extends SpellCastEvent
{
	private double cost;

	public SpellPreCastEvent(Spell spell, ICaster caster, double cost)
	{
		super(spell, caster);
		this.cost = cost;
	}

	public double getCost()
	{
		return cost;
	}

	public void setCost(double newCost)
	{
		if(newCost < 0)
			cost = 0;
		else
			cost = newCost;
	}

}
