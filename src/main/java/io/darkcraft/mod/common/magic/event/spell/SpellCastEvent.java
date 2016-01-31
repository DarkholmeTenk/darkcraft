package io.darkcraft.mod.common.magic.event.spell;

import io.darkcraft.mod.common.magic.caster.ICaster;
import io.darkcraft.mod.common.magic.spell.Spell;

public abstract class SpellCastEvent extends SpellEvent
{
	public final ICaster caster;
	public SpellCastEvent(Spell spell, ICaster caster)
	{
		super(spell);
		this.caster = caster;
	}
}
