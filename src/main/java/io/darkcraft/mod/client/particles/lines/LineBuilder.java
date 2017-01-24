package io.darkcraft.mod.client.particles.lines;

import net.minecraft.util.Vec3;

public class LineBuilder
{
	public static ILine build(Vec3 start, Vec3 end, int depth)
	{
		Line startLine = new Line(start, end);
		ILine firstSplit = startLine.split(1);
		split(firstSplit, 0, depth);
		return firstSplit;
	}

	private static void split(ILine line, int depth, int mD)
	{
		if(depth == mD)
			return;
		for(int i = 0; i < line.getNumParts(); i++)
			split(line.split(i, 0.5-(depth/(double)mD)), depth + 1, mD);
	}
}
