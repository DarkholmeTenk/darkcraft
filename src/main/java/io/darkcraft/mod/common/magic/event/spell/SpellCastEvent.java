package io.darkcraft.mod.common.magic.event.spell;

import io.darkcraft.mod.common.magic.systems.spell.Spell;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;

public abstract class SpellCastEvent extends SpellEvent
{
	public final ICaster caster;
	public SpellCastEvent(Spell spell, ICaster caster)
	{
		super(spell);
		this.caster = caster;
	}
}
