package io.darkcraft.mod.common.magic.event.spell;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.mod.common.magic.systems.spell.Spell;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;

public class SpellApplyBlockEvent extends SpellApplyEvent
{
	public final SimpleCoordStore block;

	public SpellApplyBlockEvent(ICaster caster, Spell spell, SimpleCoordStore scs)
	{
		super(caster, spell);
		block = scs;
	}

}
