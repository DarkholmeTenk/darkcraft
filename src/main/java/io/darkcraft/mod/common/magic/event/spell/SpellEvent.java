package io.darkcraft.mod.common.magic.event.spell;

import io.darkcraft.mod.common.magic.spell.Spell;
import cpw.mods.fml.common.eventhandler.Event;

public class SpellEvent extends Event
{
	public final Spell spell;
	public SpellEvent(Spell spell)
	{
		this.spell = spell;
	}
}
