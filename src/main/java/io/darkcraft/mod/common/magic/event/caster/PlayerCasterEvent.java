package io.darkcraft.mod.common.magic.event.caster;

import io.darkcraft.mod.common.magic.systems.spell.caster.PlayerCaster;

public class PlayerCasterEvent extends CasterEvent
{
	public final PlayerCaster playerCaster;
	public PlayerCasterEvent(PlayerCaster _caster)
	{
		super(_caster);
		playerCaster = _caster;
	}

}
