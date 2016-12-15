package io.darkcraft.mod.client.particles.movement;

import io.darkcraft.mod.client.particles.BasicParticle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class AbstractMovement
{
	@SideOnly(Side.CLIENT)
	protected BasicParticle entity;

	@SideOnly(Side.CLIENT)
	public final void setParticle(BasicParticle entity)
	{
		this.entity = entity;
		onParticleSet();
	}

	@SideOnly(Side.CLIENT)
	protected void onParticleSet(){}

	@SideOnly(Side.CLIENT)
	public abstract void move();
}
