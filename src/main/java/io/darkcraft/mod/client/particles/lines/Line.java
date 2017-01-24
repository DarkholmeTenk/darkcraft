package io.darkcraft.mod.client.particles.lines;

import net.minecraft.util.Vec3;

import io.darkcraft.mod.DarkcraftMod;

public class Line implements ILine
{
	public final Vec3 start;
	public final Vec3 end;

	public Line(Vec3 start, Vec3 end)
	{
		this.start = start;
		this.end = end;
	}

	@Override
	public int getNumParts()
	{
		return 0;
	}

	@Override
	public ILine getSubPart(int i)
	{
		return null;
	}

	public SubbedLine split(double newWeight)
	{
		if(DarkcraftMod.modRand.nextDouble() < newWeight)
			return new SubbedLineSplit(start, end);
		else
			return new SubbedLine(start, end);
	}

	@Override
	public ILine split(int i, double newWeight)
	{
		return this;
	}
}
