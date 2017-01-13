package io.darkcraft.mod.common.magic.systems.symbolic;

import io.darkcraft.mod.common.magic.systems.symbolic.type.SymbolicEffect;

public final class DeadEffect extends SymbolicEffect
{
	private final String reason;

	public DeadEffect(String reason)
	{
		this.reason = reason;
	}

	public String getName()
	{
		return reason;
	}

	@Override
	public String id()
	{
		return "";
	}

	@Override
	public void tick(){}

}
