package io.darkcraft.mod.client.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import io.darkcraft.darkcore.mod.datastore.Colour;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.client.particles.movement.AbstractMovement;

public abstract class ParticleCreator extends EntityFX
{

	protected ParticleCreator(int time, World w, double x, double y, double z)
	{
		super(w, x, y, z);
		particleMaxAge = time;
	}

	@Override
	public final void onUpdate()
    {
        if(isDead)
        	return;

        if (particleAge++ >= particleMaxAge)
            setDead();
        else
        	tick();
    }

	private ResourceLocation tex;
	private UVStore uv;
	private Colour colour;

	public void setRenderInfo(ResourceLocation rl, UVStore uv, Colour c)
	{
		tex = rl;
		this.uv = uv;
		colour = c;
	}

	public void setRenderInfo(ResourceLocation rl, UVStore uv)
	{
		setRenderInfo(rl, uv, RenderHelper.white);
	}

	protected BasicParticle create(int life, double x, double y, double z, AbstractMovement move)
	{
		BasicParticle particle = new BasicParticle(worldObj, x, y, z, move);
		particle.setMaxAge(life);
		if(tex != null)
			particle.setRenderInfo(tex, uv, colour);
		return particle;
	}

	public void add(BasicParticle particle)
	{
		Minecraft.getMinecraft().effectRenderer.addEffect(particle);
	}

	public abstract void tick();
}
