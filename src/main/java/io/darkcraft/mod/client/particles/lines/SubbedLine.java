package io.darkcraft.mod.client.particles.lines;

import java.util.Random;

import net.minecraft.util.Vec3;

import io.darkcraft.mod.DarkcraftMod;

public class SubbedLine implements ILine
{
	private ILine startLine;
	private ILine endLine;

	public SubbedLine(Vec3 start, Vec3 end)
	{
		Vec3 midPoint = newPoint(start, end);
		startLine = new Line(start,midPoint);
		endLine = new Line(midPoint, end);
	}

	@Override
	public int getNumParts()
	{
		return 2;
	}

	@Override
	public ILine getSubPart(int i)
	{
		if(i == 0)
			return startLine;
		else
			return endLine;
	}

	public static Vec3 newPoint(Vec3 a, Vec3 b)
	{
		Random r = DarkcraftMod.modRand;
		Vec3 d = a.subtract(b);
		double dist = d.lengthVector() / 5;
		return a.addVector(d(r,d.xCoord,dist), d(r,d.yCoord,dist), d(r,d.zCoord,dist));
	}

	private static double d(Random r, double d, double dist)
	{
		return (r.nextDouble() * d) + ((r.nextDouble() * dist) - (0.5 * dist));
	}

	@Override
	public ILine split(int i, double newWeight)
	{
		if(i == 0)
			return startLine = ((Line)startLine).split(newWeight);
		else
			return endLine = ((Line)endLine).split(newWeight);
	}
}
