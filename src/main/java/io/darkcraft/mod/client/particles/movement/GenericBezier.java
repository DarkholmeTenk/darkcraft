package io.darkcraft.mod.client.particles.movement;

import io.darkcraft.darkcore.mod.helpers.MathHelper;
import net.minecraft.util.Vec3;

public class GenericBezier extends AbstractMovement
{
	private final Vec3[] vectors;
	private final int numTicks;
	private int tickCount = 0;
	
	private final int[] coefficients;
	
	@Override
	protected void onParticleSet()
	{
		vectors[0] = Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ);
	}

	public GenericBezier(int numTicks, Vec3[] vectors)
	{
		this.vectors = new Vec3[vectors.length + 1];
		for(int i = 0; i < vectors.length; i++)
			this.vectors[i+1] = vectors[i];
		this.numTicks = numTicks;
		coefficients = MathHelper.getBinomialCoefficient(this.vectors.length);
	}

	@Override
	public void move()
	{
		tickCount++;
		double t = tickCount / (double) numTicks;
		double mt = 1 - t;
		double x = 0, y = 0, z = 0;
		double[] tVals = MathHelper.getCoefficients(mt, t, vectors.length);
		for(int i = 0; i < vectors.length; i++)
		{
			Vec3 vec = vectors[i];
			double progress = tVals[i] * coefficients[i];
			x += progress*vec.xCoord;
			y += progress*vec.yCoord;
			z += progress*vec.zCoord;
		}
		entity.setPosition(x, y, z);
	}

}
