package io.darkcraft.mod.common.magic.event.spell;

import cpw.mods.fml.common.eventhandler.Event;
import io.darkcraft.mod.common.magic.systems.spell.Spell;

public class SpellEvent extends Event
{
	public final Spell spell;
	public SpellEvent(Spell spell)
	{
		this.spell = spell;
	}
}
