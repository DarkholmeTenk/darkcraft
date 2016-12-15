package io.darkcraft.mod.client.particles.movement;

import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;

public class Path extends AbstractMovement
{
	private final SimpleDoubleCoordStore from;
	private final SimpleDoubleCoordStore to;
	private final SimpleDoubleCoordStore dist;
	private int startTime;
	private final int length;
	private int endTime;

	public Path(SimpleDoubleCoordStore from, SimpleDoubleCoordStore to, int length)
	{
		this.from = from;
		this.to = to;
		dist = to.subtract(from);
		this.length = length;
	}

	@Override
	protected void onParticleSet()
	{
		startTime = entity.getAge();
		endTime = startTime + length;
	}

	private float percent()
	{
		return (entity.getAge() - startTime) / (float) length;
	}

	@Override
	public void move()
	{
		float p = percent();
		entity.setPosition(from.x + (dist.x * p), from.y + (dist.y * p), from.z + (dist.z * p));
	}

}
