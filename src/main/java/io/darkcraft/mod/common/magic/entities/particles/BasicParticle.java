package io.darkcraft.mod.common.magic.entities.particles;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import io.darkcraft.darkcore.mod.datastore.Colour;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.common.magic.entities.particles.movement.AbstractMovement;

public class BasicParticle extends EntityFX
{
	protected AbstractMovement movement;

	private ResourceLocation tex;
	private UVStore uv;
	private Colour colour;

	public BasicParticle(World w, double x, double y, double z, AbstractMovement movement)
	{
		super(w, x, y, z);
		this.movement = movement;
		movement.setParticle(this);
		particleMaxAge = 200;
		setParticleTextureIndex(82); // same as happy villager
        particleScale = 1.0F;
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
        float sz = 0.1F * particleScale;

        float pX = (float)((prevPosX + ((posX - prevPosX) * ptt)) - interpPosX);
        float pY = (float)((prevPosY + ((posY - prevPosY) * ptt)) - interpPosY);
        float pZ = (float)((prevPosZ + ((posZ - prevPosZ) * ptt)) - interpPosZ);
        tess.setColorRGBA_F(particleRed, particleGreen, particleBlue, particleAlpha);
        RenderHelper.colour(colour);
        tess.addVertexWithUV(handle(pX, -rotationX, -rotationYZ, sz), pY - (rotationXZ * sz), handle(pZ, -rotationZ, -rotationXY, sz), uv.U, uv.V);
        tess.addVertexWithUV(handle(pX, -rotationX,  rotationYZ, sz), pY + (rotationXZ * sz), handle(pZ, -rotationZ,  rotationXY, sz), uv.U, uv.v);
        tess.addVertexWithUV(handle(pX,  rotationX,  rotationYZ, sz), pY + (rotationXZ * sz), handle(pZ,  rotationZ,  rotationXY, sz), uv.u, uv.v);
        tess.addVertexWithUV(handle(pX,  rotationX, -rotationYZ, sz), pY - (rotationXZ * sz), handle(pZ,  rotationZ, -rotationXY, sz), uv.u, uv.V);
        RenderHelper.resetColour();
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
