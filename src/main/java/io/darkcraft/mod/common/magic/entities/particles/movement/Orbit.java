package io.darkcraft.mod.common.magic.entities.particles.movement;

import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.darkcore.mod.interfaces.IPositionProvider;

public class Orbit extends AbstractFollower
{
	private double distance = -1;
	private double angle = 0;
	private final double orbitSpeed;

	public Orbit(IPositionProvider provider, double orbitSpeed)
	{
		super(provider);
		this.orbitSpeed = orbitSpeed;
	}

	@Override
	protected void onParticleSet()
	{
		SimpleDoubleCoordStore centerPos = provider.getPosition();
		SimpleDoubleCoordStore partPos = new SimpleDoubleCoordStore(entity);
		distance = centerPos.distance(partPos);
		angle = Math.atan2(partPos.z - centerPos.z, partPos.x - centerPos.x);
	}

	@Override
	public void follow(SimpleDoubleCoordStore pos)
	{
		angle += orbitSpeed;
		double x = distance * MathHelper.sin(angle);
		double z = distance * MathHelper.cos(angle);
		entity.setPosition(pos.x + x, pos.y, pos.z + z);
	}



}
