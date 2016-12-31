package io.darkcraft.mod.proxy;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import io.darkcraft.apt.ClientMethod;
import io.darkcraft.apt.ClientMethod.Broadcast;
import io.darkcraft.apt.CommonProxy;
import io.darkcraft.darkcore.mod.datastore.Colour;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.handlers.containers.IEntityContainer;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.darkcore.mod.proxy.BaseProxy;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.particles.BasicParticle;
import io.darkcraft.mod.client.particles.ParticleCreator;
import io.darkcraft.mod.client.particles.creators.BlockCreator;
import io.darkcraft.mod.client.particles.creators.SpellCreatorCorner;
import io.darkcraft.mod.client.particles.movement.AbstractMovement;
import io.darkcraft.mod.client.particles.movement.Orbit;
import io.darkcraft.mod.client.particles.movement.Towards;
import io.darkcraft.mod.client.particles.movement.Velocity;
import io.darkcraft.mod.common.magic.blocks.tileent.SpellCreator;
import io.darkcraft.mod.common.magic.entities.EntitySpellProjectile;
import io.darkcraft.mod.common.magic.systems.component.impl.Blink;
import io.darkcraft.mod.common.magic.systems.component.impl.SoulTrap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@CommonProxy
public abstract class ParticleHandler extends BaseProxy
{
	protected static final Random rand = DarkcraftMod.modRand;

	@ClientMethod(broadcast = Broadcast.DIMENSION)
	public abstract void createProjectileParticle(EntitySpellProjectile projectile);

	@ClientMethod(broadcast = Broadcast.DIMENSION)
	public abstract void createBlockCreator(SimpleCoordStore bL, SimpleCoordStore tR);

	@ClientMethod
	public abstract void createBlinkParticles(SimpleDoubleCoordStore from, SimpleDoubleCoordStore to, boolean middle);

	@ClientMethod(broadcast = Broadcast.DIMENSION)
	public abstract void createSoulTrapParticles(IEntityContainer<EntityLivingBase> soultrapped);

	@ClientMethod(broadcast = Broadcast.DIMENSION)
	public abstract void createSpellCreateParticles(SimpleCoordStore spellCreator, String[] hasUser);

	@ClientMethod(broadcast = Broadcast.DIMENSION)
	public abstract void createJumpParticles(SimpleDoubleCoordStore simpleDoubleCoordStore, Vec3 createVectorHelper);

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

		private BasicParticle create(int life, Colour c, SimpleDoubleCoordStore from, double random, AbstractMovement move)
		{
			double x = (from.x + (rand.nextDouble() * (random * 2))) - random;
			double y = (from.y + (rand.nextDouble() * (random * 2))) - random;
			double z = (from.z + (rand.nextDouble() * (random * 2))) - random;
			BasicParticle bp = new BasicParticle(from.getWorldObj(),x,y,z, move);
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
			if(bL.world != WorldHelper.getClientWorldID())
				return;
			BlockCreator bc = new BlockCreator(20, WorldHelper.getClientWorld(), bL, tR);
			bc.setRenderInfo(rl, UVStore.defaultUV, new Colour(0.1f, 0.2f, 1));
			add(bc);
		}

		@Override
		public void createBlinkParticles(SimpleDoubleCoordStore from, SimpleDoubleCoordStore to, boolean middle)
		{
			if(from.getWorldObj() != null)
				for(int i = 0; i < 20; i++)
					add(create(20+rand.nextInt(10), Blink.PARTICLE_COLOUR, from, 1, new Towards(from, 0.03)));
			if(to.getWorldObj() != null)
			{
				for(int i = 0; i < 20; i++)
					add(create(20+rand.nextInt(10), Blink.PARTICLE_COLOUR, to, 0.0001, new Towards(to, -0.05)));
				if(middle && (from.world == to.world))
				{
					double dist = from.distance(to);
					if(dist == 0)
						return;
					Vec3 vel = MathHelper.getVecBetween(from, to).normalize();
					for(int i = 0; i < dist; i++)
						add(create(40+rand.nextInt(20), Blink.PARTICLE_COLOUR, from.interpolate(to, i / dist), 0.2, new Velocity(vel, 0.1)));
				}
			}
		}

		@Override
		public void createSoulTrapParticles(IEntityContainer<EntityLivingBase> soultrapped)
		{
			for(int i = 0; i < (rand.nextInt(5) + 1); i++)
			{
				double r = (rand.nextDouble() * 360);
				double x = MathHelper.sin(r);
				double z = MathHelper.cos(r);
				SimpleDoubleCoordStore pos = soultrapped.getPosition();
				BasicParticle bp = create(20+rand.nextInt(20), SoulTrap.PARTICLE_COLOUR, pos.getWorldObj(), pos.x + x, pos.y, pos.z + z, new Orbit(soultrapped, 5));
				add(bp);
			}
		}

		@Override
		public void createSpellCreateParticles(SimpleCoordStore spellCreator, String[] players)
		{
			TileEntity te = spellCreator.getTileEntity();
			if(te instanceof SpellCreator)
			{
				final SpellCreator sc = (SpellCreator) te;
				for(int i = 0; i < 4; i++)
				{
					ParticleCreator pc = new SpellCreatorCorner(20, sc, i);
					pc.setRenderInfo(rl, UVStore.defaultUV, new Colour(0.1f, 0.2f, 1));
					add(pc);
				}
			}
		}

		@Override
		public void createJumpParticles(SimpleDoubleCoordStore simpleDoubleCoordStore, Vec3 newMotion)
		{
			for( int i = 0; i < 50; i++)
			{
				Velocity v = new Velocity(newMotion.xCoord * -0.2, 0.1, newMotion.zCoord * -0.2);
				BasicParticle bp = create(20 + rand.nextInt(20), new Colour(0.8f, 0.8f, 0.2f), simpleDoubleCoordStore, 1, v);
				add(bp);
			}
		}
	}
}
