package io.darkcraft.mod.proxy;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import io.darkcraft.apt.ClientMethod;
import io.darkcraft.apt.ClientMethod.Broadcast;
import io.darkcraft.apt.CommonProxy;
import io.darkcraft.darkcore.mod.datastore.Colour;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.darkcore.mod.proxy.BaseProxy;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.entities.EntitySpellProjectile;
import io.darkcraft.mod.common.magic.entities.particles.BasicParticle;
import io.darkcraft.mod.common.magic.entities.particles.creators.BlockCreator;
import io.darkcraft.mod.common.magic.entities.particles.movement.AbstractMovement;
import io.darkcraft.mod.common.magic.entities.particles.movement.Orbit;
import io.darkcraft.mod.common.magic.entities.particles.movement.Velocity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@CommonProxy
public class ParticleHandler extends BaseProxy
{
	protected static final Random rand = DarkcraftMod.modRand;

	@ClientMethod(broadcast = Broadcast.DIMENSION)
	public void createProjectileParticle(EntitySpellProjectile projectile){}

	@ClientMethod(broadcast = Broadcast.DIMENSION)
	public void createBlockCreator(SimpleCoordStore bL, SimpleCoordStore tR){}

	@SideOnly(Side.CLIENT)
	public static class ClientParticleHandler extends ParticleHandler
	{
		private static ResourceLocation rl = new ResourceLocation(DarkcraftMod.modName, "textures/project/genericParticle.png");

		private BasicParticle create(int life, World w, double x, double y, double z, AbstractMovement move)
		{
			return create(life, RenderHelper.white, w, x, y, z, move);
		}

		private BasicParticle create(int life, Colour c, World w, double x, double y, double z, AbstractMovement move)
		{
			BasicParticle bp = new BasicParticle(w,x,y,z, move);
			bp.setMaxAge(life);
			bp.setRenderInfo(rl, UVStore.defaultUV, c);
			return bp;
		}

		private void add(EntityFX fx)
		{
			Minecraft.getMinecraft().effectRenderer.addEffect(fx);
		}

		@Override
		public void createProjectileParticle(EntitySpellProjectile pr)
		{
			double r = (rand.nextDouble() * 360);
			double x = MathHelper.sin(r);
			double z = MathHelper.cos(r);
			Velocity vel = new Velocity(pr.motionX * 0.7, pr.motionY * 0.7, pr.motionZ * 0.7);
			vel.gravity = true;
			BasicParticle particle = create(100, pr.worldObj, pr.posX + x, pr.posY, pr.posZ + z, vel);
			add(particle);
			Orbit orbit = new Orbit(pr, 5, pr);
			add(create(200, pr.worldObj, pr.posX + x, pr.posY, pr.posZ + z, orbit));
		}

		@Override
		public void createBlockCreator(SimpleCoordStore bL, SimpleCoordStore tR)
		{
			BlockCreator bc = new BlockCreator(20, bL.getWorldObj(), bL, tR);
			bc.setRenderInfo(rl, UVStore.defaultUV, new Colour(0.1f, 0.2f, 1));
			add(bc);
		}
	}
}
