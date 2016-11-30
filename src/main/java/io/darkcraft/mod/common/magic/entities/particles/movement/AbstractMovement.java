package io.darkcraft.mod.common.magic.entities.particles.movement;

import io.darkcraft.mod.common.magic.entities.particles.BasicParticle;

public abstract class AbstractMovement
{
	protected BasicParticle entity;

	public final void setParticle(BasicParticle entity)
	{
		this.entity = entity;
		onParticleSet();
	}

	protected void onParticleSet(){}

	public abstract void move();
}
