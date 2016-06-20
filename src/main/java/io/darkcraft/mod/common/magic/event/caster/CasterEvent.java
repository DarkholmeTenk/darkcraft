package io.darkcraft.mod.common.magic.event.caster;

import cpw.mods.fml.common.eventhandler.Event;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;

public class CasterEvent extends Event
{
	public final ICaster caster;

	public CasterEvent(ICaster _caster)
	{
		caster = _caster;
	}
}
