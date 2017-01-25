package io.darkcraft.mod.client.particles.lines;

import net.minecraft.util.Vec3;

public class SubbedLineSplit extends SubbedLine
{
	private ILine newLine;

	public SubbedLineSplit(Vec3 start, Vec3 end)
	{
		super(start, end);
		Vec3 newPoint = newPoint(start, midPoint);
		newLine = new Line(start,newPoint);
	}

	@Override
	public int getNumParts()
	{
		return 3;
	}

	@Override
	public ILine getSubPart(int i)
	{
		if(i == 2)
			return newLine;
		else
			return super.getSubPart(i);
	}

	@Override
	public ILine split(int i, double newWeight)
	{
		if(i == 2)
			return newLine = ((Line)newLine).split(newWeight / 2);
		return super.split(i, newWeight);
	}
}
