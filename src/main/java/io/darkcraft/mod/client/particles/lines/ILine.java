package io.darkcraft.mod.client.particles.lines;

public interface ILine
{
	public int getNumParts();

	public ILine getSubPart(int i);

	public ILine split(int i, double newWeight);
}
