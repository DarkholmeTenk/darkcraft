package io.darkcraft.mod.common.magic.event.spell;

import io.darkcraft.mod.common.magic.caster.ICaster;
import io.darkcraft.mod.common.magic.spell.Spell;


public abstract class SpellApplyEvent extends SpellCastEvent
{
	public final double[] spellMagnitudeMults;
	public final double[] spellDurationMults;

	public SpellApplyEvent(ICaster caster, Spell spell)
	{
		super(spell, caster);
		spellMagnitudeMults = new double[spell.components.length];
		spellDurationMults = new double[spell.components.length];
		for(int i = 0; i < spell.components.length; i++)
		{
			spellMagnitudeMults[i] = 1;
			spellDurationMults[i] = 1;
		}
	}

}
