package io.darkcraft.mod.client.particles;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import io.darkcraft.darkcore.mod.datastore.Colour;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.mod.client.particles.movement.AbstractMovement;

public class BasicParticle extends EntityFX
{
	protected AbstractMovement movement;

	private ResourceLocation tex;
	private UVStore uv;
	private Colour colour;

	private float lastScale = 0;

	public BasicParticle(World w, double x, double y, double z, AbstractMovement movement)
	{
		super(w.isRemote ? w : WorldHelper.getClientWorld(), x, y, z);
		this.movement = movement;
		movement.setParticle(this);
		particleMaxAge = 200;
		setParticleTextureIndex(82); // same as happy villager
        particleScale = 0;
        particleGravity = 0.05f;

        movement.move();
	}

	public float getGravity()
	{
		return particleGravity;
	}

	public int getAge()
	{
		return particleAge;
	}

	public int getMaxAge()
	{
		return particleMaxAge;
	}

	public void setMaxAge(int newMaxAge)
	{
		particleMaxAge = newMaxAge;
	}

	private double handle(float a, float rotA, float rotB, float size)
	{
		return a + (rotA * size) + (rotB * size);
	}

	@Override
	public void renderParticle(Tessellator tess, float ptt,
			float rotationX, float rotationXZ, float rotationZ, float rotationYZ, float rotationXY)
    {
		if(isDead)
			return;
		if(tex == null)
		{
			super.renderParticle(tess, ptt, rotationX, rotationXZ, rotationZ, rotationYZ, rotationXY);
			return;
		}
		RenderHelper.bindTexture(tex);
        float sz = 0.1F * MathHelper.interpolate(lastScale, particleScale, ptt);

        float pX = (float)((prevPosX + ((posX - prevPosX) * ptt)) - interpPosX);
        float pY = (float)((prevPosY + ((posY - prevPosY) * ptt)) - interpPosY);
        float pZ = (float)((prevPosZ + ((posZ - prevPosZ) * ptt)) - interpPosZ);
        RenderHelper.colour(tess, colour);
        tess.addVertexWithUV(handle(pX, -rotationX, -rotationYZ, sz), pY - (rotationXZ * sz), handle(pZ, -rotationZ, -rotationXY, sz), uv.U, uv.V);
        tess.addVertexWithUV(handle(pX, -rotationX,  rotationYZ, sz), pY + (rotationXZ * sz), handle(pZ, -rotationZ,  rotationXY, sz), uv.U, uv.v);
        tess.addVertexWithUV(handle(pX,  rotationX,  rotationYZ, sz), pY + (rotationXZ * sz), handle(pZ,  rotationZ,  rotationXY, sz), uv.u, uv.v);
        tess.addVertexWithUV(handle(pX,  rotationX, -rotationYZ, sz), pY - (rotationXZ * sz), handle(pZ,  rotationZ, -rotationXY, sz), uv.u, uv.V);
        RenderHelper.resetColour(tess);
    }

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

	@Override
	public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        if(isDead)
        	return;

        lastScale = particleScale;
        if(particleMaxAge > 8)
        {
        	if(particleAge <= 4)
        		particleScale = particleAge / 4f;
        	else if(particleAge >= (particleMaxAge - 4))
        		particleScale = (particleMaxAge - particleAge - 1) / 4f;
        	else
        		particleScale = 1;
        }

        if (particleAge++ >= particleMaxAge)
        {
            setDead();
        }
        else
        {
        	movement.move();
        }
    }

	@Override
	public void moveEntity(double x, double y, double z)
    {
		try
		{
			super.moveEntity(x, y, z);
		}
		catch(NullPointerException e)
		{
			setDead();
		}
    }
}
