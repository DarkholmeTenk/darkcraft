package io.darkcraft.mod.common.magic.event.caster;

import io.darkcraft.mod.common.magic.systems.spell.caster.PlayerCaster;

public class PlayerCasterManaRegenEvent extends PlayerCasterEvent
{
	public double regenAmount;

	public PlayerCasterManaRegenEvent(PlayerCaster _caster, double toRegen)
	{
		super(_caster);
		regenAmount = toRegen;
	}

}
