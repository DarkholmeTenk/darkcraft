package io.darkcraft.mod.client.particles.lines;

import net.minecraft.util.Vec3;

public class LineBuilder
{
	public static ILine build(Vec3 start, Vec3 end, int depth)
	{
		Line startLine = new Line(start, end);
		ILine firstSplit = startLine.split(0);
		split(firstSplit, 0, depth);
		return firstSplit;
	}

	private static ILine split(ILine line, int i, int max, int depth, int mD)
	{
		double depthP = depth / (double)mD;
		double chance = 0.8-depthP;
		if(max == 3)
		{
			if(i == 1)
				chance = 1 - depthP;
			else
				chance = 0;
		}
		chance = 0;
		return line.split(i, chance);
	}

	private static void split(ILine line, int depth, int mD)
	{
		if(depth >= mD)
			return;
		int max = line.getNumParts();
		for(int i = 0; i < max; i++)
			split(split(line,i,max,depth,mD), i == 2 ? depth + 2 : depth + 1, mD);
	}
}
