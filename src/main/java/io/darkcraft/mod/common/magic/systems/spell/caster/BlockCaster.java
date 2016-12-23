package io.darkcraft.mod.common.magic.systems.spell.caster;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.mod.common.magic.entities.EntitySpellProjectile;
import io.darkcraft.mod.common.magic.event.spell.SpellPreCastEvent;
import io.darkcraft.mod.common.magic.systems.spell.CastType;
import io.darkcraft.mod.common.magic.systems.spell.Spell;
import io.darkcraft.mod.common.registries.MagicConfig;

public class BlockCaster implements ICaster
{
	public final SimpleCoordStore blockPos;
	public final SimpleDoubleCoordStore blockCenter;
	public final IBlockCasterHandler handler;

	public BlockCaster(SimpleCoordStore pos, IBlockCasterHandler _handler)
	{
		blockPos = pos;
		blockCenter = pos.getCenter();
		handler = _handler;
	}

	@Override
	public double getMana()
	{
		return handler.getMana();
	}

	@Override
	public double getMaxMana()
	{
		return handler.getMaxMana();
	}

	@Override
	public boolean useMana(double amount, boolean sim)
	{
		return true;
	}

	public SimpleDoubleCoordStore getSpellCreationPos()
	{
		return blockCenter;
	}

	public void setVelocity(EntitySpellProjectile sp)
	{
		SimpleDoubleCoordStore target = handler.getProjectileTarget();
		Vec3 vec = MathHelper.getVecBetween(blockCenter, target);
		vec.normalize();
		double speed = MagicConfig.projectileSpeed;
		sp.motionX = vec.xCoord * speed;
		sp.motionY = vec.yCoord * speed;
		sp.motionZ = vec.zCoord * speed;
		sp.velocityChanged = true;
	}

	public ForgeDirection getFacing()
	{
		return handler.getFacing();
	}

	private void castProjectile(Spell spell)
	{
		if(castTouch(spell)) return;
		SimpleDoubleCoordStore dcs = getSpellCreationPos();
		if(dcs == null) return;
		EntitySpellProjectile esp = new EntitySpellProjectile(this, spell, dcs);
		setVelocity(esp);
		dcs.getWorldObj().spawnEntityInWorld(esp);
	}

	private boolean castTouch(Spell spell)
	{
		SimpleCoordStore hitPos = blockPos.getNearby(getFacing());
		AxisAlignedBB aabb = hitPos.getCenter().getAABB(0.5);
		List<Entity> ents = blockPos.getWorldObj().getEntitiesWithinAABB(Entity.class, aabb);
		if((ents == null) || (ents.size() == 0))
		{
			Block b = hitPos.getBlock();
			int m = hitPos.getMetadata();
			if(b == null) return false;
			if(MagicConfig.traceLiquids || b.canCollideCheck(m, false))
			{
				spell.apply(this, hitPos, -1);
				return true;
			}
		}
		else
		{
			SimpleDoubleCoordStore cent = blockCenter;
			Entity closest = null;
			double bestDist = Double.MAX_VALUE;
			for(Entity e : ents)
			{
				if(e == null) continue;
				SimpleDoubleCoordStore d = new SimpleDoubleCoordStore(e);
				double dist = cent.distance(d);
				if(dist < bestDist)
				{
					bestDist = dist;
					closest = e;
				}
			}
			if(closest != null)
			{
				spell.apply(this, closest);
				return true;
			}
		}
		return false;
	}

	private void castSelf(Spell spell)
	{
		spell.apply(this, blockPos, 0);
	}

	private boolean doCast(Spell spell)
	{
		double cost = spell.getCost(this);
		SpellPreCastEvent spce = new SpellPreCastEvent(spell, this, cost);
		MinecraftForge.EVENT_BUS.post(spce);
		if(!spce.isCanceled())
			return (useMana(spce.getCost(), false));
		return false;
	}

	@Override
	public void cast(Spell spell, boolean useMana)
	{
		if(doCast(spell))
		{
			if(spell.type == CastType.PROJECTILE)
				castProjectile(spell);
			else if(spell.type == CastType.TOUCH)
				castTouch(spell);
			else if(spell.type == CastType.SELF)
				castSelf(spell);
		}
	}

	@Override
	public void cast(Spell spell, SimpleCoordStore block, int side, boolean useMana)
	{
		if(block == null) return;
		if((spell.type == CastType.SELF) && !(block.equals(blockPos))) return;
		if(doCast(spell))
			spell.apply(this, block, 0);
	}

	@Override
	public void cast(Spell spell, Entity ent, boolean useMana)
	{
		if(ent == null) return;
		if(spell.type == CastType.SELF) return;
		if(doCast(spell))
			spell.apply(this, ent);
	}

	@Override
	public SimpleDoubleCoordStore getCoords()
	{
		return blockPos.getCenter();
	}

	@Override
	public double addMana(double amount, boolean sim)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public void tick()
	{

	}

	public void writeToNBT(NBTTagCompound nbt)
	{

	}

	public void readFromNBT(NBTTagCompound nbt)
	{

	}

	@Override
	public boolean isCaster(SimpleCoordStore pos)
	{
		return blockPos.equals(pos);
	}

	@Override
	public boolean isCaster(Entity ent)
	{
		return false;
	}
}
