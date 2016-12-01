package io.darkcraft.mod.common.magic.entities.particles.movement;

import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;

import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.darkcore.mod.interfaces.IPositionProvider;

public class Orbit extends AbstractFollower
{
	private final static Vec3 UP_NORMAL = Vec3.createVectorHelper(0,1,0);

	private double distance = -1;
	private double angle = 0;
	private final double orbitSpeed;
	private final Vec3 normal;

	private final float pitch;
	private final float yaw;

	public Orbit(IPositionProvider provider, double orbitSpeed)
	{
		this(provider, orbitSpeed, UP_NORMAL);
	}

	public Orbit(IPositionProvider provider, double orbitSpeed, double nx, double ny, double nz)
	{
		this(provider, orbitSpeed, Vec3.createVectorHelper(nx, ny, nz).normalize());
	}

	public Orbit(IPositionProvider provider, double orbitSpeed, Entity ent)
	{
		this(provider, orbitSpeed, ent.motionX, ent.motionY, ent.motionZ);
	}

	public Orbit(IPositionProvider provider, double orbitSpeed, Vec3 normal)
	{
		super(provider);
		this.orbitSpeed = orbitSpeed;
		this.normal = normal;

		if(normal == UP_NORMAL)
		{
			pitch = 0;
			yaw = 0;
		}
		else
		{
			double horiz = Vec3.createVectorHelper(normal.xCoord, 0, normal.zCoord).lengthVector();
			double verti = Vec3.createVectorHelper(0, normal.yCoord, 0).lengthVector();
			pitch = (float) Math.atan2(horiz, verti);
			yaw = (float) Math.atan2(normal.xCoord, normal.zCoord);
		}
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
		Vec3 nv = Vec3.createVectorHelper(x, 0, z);
		if(normal != UP_NORMAL)
		{
			nv.rotateAroundX(pitch);
			nv.rotateAroundY(yaw);
		}
		entity.setPosition(pos.x + nv.xCoord, pos.y + nv.yCoord, pos.z + nv.zCoord);
	}



}
