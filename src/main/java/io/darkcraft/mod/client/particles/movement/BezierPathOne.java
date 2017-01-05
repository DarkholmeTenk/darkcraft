package io.darkcraft.mod.client.particles.movement;

import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;

public class BezierPathOne extends AbstractMovement
{
	private SimpleDoubleCoordStore start;
	private final SimpleDoubleCoordStore controlPoint;
	private final SimpleDoubleCoordStore end;
	private final int numTicks;
	private int tick = 0;
	
	public BezierPathOne(int numTicks, SimpleDoubleCoordStore end, SimpleDoubleCoordStore point)
	{
		this.numTicks = numTicks;
		this.end = end;
		this.controlPoint = point;
	}
	
	private double getNum(int switcher, SimpleDoubleCoordStore point)
	{
		switch(switcher)
		{
		case 0: return point.x;
		case 1: return point.y;
		case 2: return point.z;
		}
		return 0;
	}
	
	private double getCoord(int switcher, double tmt, double mt2, double t2)
	{
		return mt2*getNum(switcher,start) + 2*tmt*getNum(switcher,controlPoint) + t2*getNum(switcher, end);
	}

	@Override
	public void move()
	{
		tick++;
		double t = (tick / numTicks);
		double mt = 1 - t;
		double t2 = t*t;
		double tmt = t*mt;
		double mt2 = mt * mt;
		double x = getCoord(0, tmt, mt2, t2);
		double y = getCoord(1, tmt, mt2, t2);
		double z = getCoord(2, tmt, mt2, t2);
		entity.setPosition(x, y, z);
	}

	@Override
	protected void onParticleSet()
	{
		start = new SimpleDoubleCoordStore(entity);
	}

}
