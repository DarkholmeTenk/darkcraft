package io.darkcraft.mod.client.particles.movement;

import net.minecraft.util.Vec3;

import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;

public class Towards extends AbstractMovement
{
	private final SimpleDoubleCoordStore pos;
	private final double speed;

	private Vec3 movement;

	public Towards(SimpleDoubleCoordStore pos, double speed)
	{
		this.pos = pos;
		this.speed = speed;
	}

	@Override
	protected void onParticleSet()
	{
		SimpleDoubleCoordStore myPos = new SimpleDoubleCoordStore(entity);
		Vec3 to = Vec3.createVectorHelper(pos.x - myPos.x, pos.y - myPos.y, pos.z - myPos.z).normalize();
		movement = Vec3.createVectorHelper(to.xCoord * speed, to.yCoord * speed, to.zCoord * speed);
	}

	@Override
	public void move()
	{
		if(movement == null)
			return;
		entity.moveEntity(movement.xCoord, movement.yCoord, movement.zCoord);
	}

}
