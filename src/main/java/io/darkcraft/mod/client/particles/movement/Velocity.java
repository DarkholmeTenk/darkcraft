package io.darkcraft.mod.client.particles.movement;

import net.minecraft.util.Vec3;

public class Velocity extends AbstractMovement
{
	public double motionX;
	public double motionY;
	public double motionZ;

	public double airFriction = 0.98;
	public double groundFriction = 0.7;

	public boolean gravity = true;

	public Velocity(double motionX, double motionY, double motionZ)
	{
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
	}

	public Velocity(Vec3 normalisedVec, double speed)
	{
		this(normalisedVec.xCoord * speed, normalisedVec.yCoord * speed, normalisedVec.zCoord * speed);
	}

	@Override
	public void move()
	{
		if(gravity)
			motionY -= 0.4 * entity.getGravity();
		entity.moveEntity(motionX, motionY, motionZ);

		motionX *= airFriction;
		motionY *= airFriction;
		motionZ *= airFriction;

		if(entity.onGround)
		{
			motionX *= groundFriction;
			motionZ *= groundFriction;
		}
	}
}
