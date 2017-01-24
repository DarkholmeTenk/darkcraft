package io.darkcraft.mod.client.particles.lines;

import java.util.LinkedList;

import com.google.common.collect.AbstractIterator;

import io.darkcraft.darkcore.mod.datastore.Pair;

public class LineIterator extends AbstractIterator<Line>
{
	private LinkedList<Pair<ILine, Integer>> list = new LinkedList<>();

	public LineIterator(ILine start)
	{
		list.add(new Pair<>(start, -1));
	}

	@Override
	protected Line computeNext()
	{
		while(!list.isEmpty())
		{
			Pair<ILine, Integer> blob = list.removeFirst();
			if(blob.a instanceof Line)
				return (Line) blob.a;
			blob = new Pair(blob.a, blob.b+1);
			if(blob.b == blob.a.getNumParts())
				continue;
			list.addFirst(blob);
			list.addFirst(new Pair(blob.a.getSubPart(blob.b), -1));
		}
		return endOfData();
	}

}
