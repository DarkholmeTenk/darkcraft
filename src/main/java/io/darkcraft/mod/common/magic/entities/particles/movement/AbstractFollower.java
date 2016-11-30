package io.darkcraft.mod.common.magic.entities.particles.movement;

import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.interfaces.IPositionProvider;

public abstract class AbstractFollower extends AbstractMovement
{
	public IPositionProvider provider;

	public AbstractFollower(IPositionProvider provider)
	{
		this.provider = provider;
	}

	@Override
	public final void move()
	{
		SimpleDoubleCoordStore pos = provider.getPosition();
		if(pos == null)
			entity.setDead();
		else
			follow(pos);
	}

	public abstract void follow(SimpleDoubleCoordStore pos);
}
