package io.darkcraft.mod.client.particles.movement;

import java.util.List;

import com.google.common.collect.Lists;

public class CompositeMovement extends AbstractMovement
{
	private int slot = 0;
	private int max = 0;

	private List<AbstractMovement> moves = Lists.newArrayList();
	private List<Integer> lives = Lists.newArrayList();
	private int maxLife = 0;

	public CompositeMovement(int life, AbstractMovement firstMove)
	{
		addMove(life, firstMove);
	}

	public CompositeMovement addMove(int life, AbstractMovement move)
	{
		moves.add(move);
		lives.add(maxLife += life);
		max++;
		return this;
	}

	private AbstractMovement getMovement()
	{
		if(slot >= max) return null;
		int age = entity.getAge();
		Integer live = lives.get(slot);
		if(age >= live)
		{
			slot++;
			moves.get(slot).setParticle(entity);
		}
		if(slot >= max)
			return null;
		return moves.get(slot);
	}

	@Override
	public void move()
	{
		AbstractMovement movement = getMovement();
		if(movement == null)
			return;
		movement.move();
	}
}
