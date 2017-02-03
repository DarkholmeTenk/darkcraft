package io.darkcraft.mod.client.particles;

import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import io.darkcraft.darkcore.mod.datastore.Colour;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.mod.client.particles.lines.ILine;
import io.darkcraft.mod.client.particles.lines.Line;
import io.darkcraft.mod.client.particles.lines.LineBuilder;
import io.darkcraft.mod.client.particles.lines.LineIterator;

public class Lightning extends EntityFX
{
	private Colour colour = Colour.white;

	private final ILine line;

	public Lightning(World w, double x, double y, double z, double dx, double dy, double dz)
	{
		super(w.isRemote ? w : WorldHelper.getClientWorld(), x, y, z);
		particleMaxAge = 50;
		line = LineBuilder.build(Vec3.createVectorHelper(0, 0, 0), Vec3.createVectorHelper(dx-x, dy-y, dz-z), 6);
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

	@Override
	public void renderParticle(Tessellator tess, float ptt,
			float rotationX, float rotationXZ, float rotationZ, float rotationYZ, float rotationXY)
    {
		if(isDead)
			return;
		tess.draw();
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		tess.startDrawing(GL11.GL_LINES);
		RenderHelper.colour(tess, colour);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		Iterator<Line> lineIter = new LineIterator(line);
		EntityLivingBase entitylivingbase1 = Minecraft.getMinecraft().thePlayer;
		float pX = 0;float pY = 0;float pZ = 0;
		pX = (float) (-((entitylivingbase1.lastTickPosX) + ((entitylivingbase1.posX - entitylivingbase1.lastTickPosX) * ptt))+posX);
		pY = (float) (-((entitylivingbase1.lastTickPosY) + ((entitylivingbase1.posY - entitylivingbase1.lastTickPosY) * ptt))+posY);
		pZ = (float) (-((entitylivingbase1.lastTickPosZ) + ((entitylivingbase1.posZ - entitylivingbase1.lastTickPosZ) * ptt))+posZ);
		while(lineIter.hasNext())
		{
			Line line = lineIter.next();
			Vec3 start = line.start;
			Vec3 end = line.end;
			tess.addVertex(start.xCoord + pX, start.yCoord + pY, start.zCoord + pZ);
			tess.addVertex(end.xCoord + pX, end.yCoord + pY, end.zCoord + pZ);
		}
		tess.draw();
		GL11.glPopAttrib();
		tess.startDrawingQuads();
    }

	@Override
	public void onUpdate()
    {
		particleAge++;
		if(getAge() > getMaxAge())
			setDead();
    }

	@Override
	public int getFXLayer()
	{
		return 1;
	}
}
