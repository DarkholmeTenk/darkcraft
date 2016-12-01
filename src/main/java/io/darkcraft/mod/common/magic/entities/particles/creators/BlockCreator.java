package io.darkcraft.mod.common.magic.entities.particles.creators;

import net.minecraft.world.World;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.mod.common.magic.entities.particles.ParticleCreator;
import io.darkcraft.mod.common.magic.entities.particles.movement.AbstractMovement;
import io.darkcraft.mod.common.magic.entities.particles.movement.Path;

public class BlockCreator extends ParticleCreator
{
	private final SimpleDoubleCoordStore bL;
	private final SimpleDoubleCoordStore tR;

	public BlockCreator(int time, World w, SimpleCoordStore bL, SimpleCoordStore tR)
	{
		super(time, w, bL.x, bL.y, bL.z);
		this.bL = new SimpleDoubleCoordStore(bL.world, bL.x, bL.y, bL.z);
		this.tR = new SimpleDoubleCoordStore(tR.world, tR.x+1, tR.y+1, tR.z+1);
	}

	private AbstractMovement getMovement(int l)
	{
		boolean x,y,z;
		SimpleDoubleCoordStore from = new SimpleDoubleCoordStore(bL.world,
				(x = rand.nextBoolean()) ? bL.x : tR.x,
				(y = rand.nextBoolean()) ? bL.y : tR.y,
				(z = rand.nextBoolean()) ? bL.z : tR.z);
		switch(rand.nextInt(3))
		{
			case 0: x = !x; break;
			case 1: y = !y; break;
			case 2: z = !z; break;
		}
		SimpleDoubleCoordStore to = new SimpleDoubleCoordStore(bL.world,
				x ? bL.x : tR.x,
				y ? bL.y : tR.y,
				z ? bL.z : tR.z);
		return new Path(from, to, l);
	}

	@Override
	public void tick()
	{
		add(create(20, posX, posY, posZ, getMovement(20)));
	}

}
