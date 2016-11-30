package io.darkcraft.mod.common.magic.entities.particles;

import io.darkcraft.mod.common.magic.entities.particles.movement.AbstractMovement;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

public class BasicParticle extends EntityFX
{
	protected AbstractMovement movement;

	public BasicParticle(World w, double x, double y, double z, AbstractMovement movement)
	{
		super(w, x, y, z);
		this.movement = movement;
		movement.setParticle(this);
		particleMaxAge = 200;
		setParticleTextureIndex(82); // same as happy villager
        particleScale = 2.0F;
        particleGravity = 0.01f;
	}

	public float getGravity()
	{
		return particleGravity;
	}

	@Override
	public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        if(isDead)
        	return;

        if (particleAge++ >= particleMaxAge)
        {
            setDead();
        }
        else
        {
        	movement.move();
        }
    }
}
