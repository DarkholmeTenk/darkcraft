package io.darkcraft.mod.common.magic.caster;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.mod.common.magic.entities.EntitySpellProjectile;
import io.darkcraft.mod.common.magic.event.spell.SpellPreCastEvent;
import io.darkcraft.mod.common.magic.spell.CastType;
import io.darkcraft.mod.common.magic.spell.Spell;
import io.darkcraft.mod.common.registries.MagicConfig;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockCaster implements ICaster
{
	public final SimpleCoordStore blockPos;

	public BlockCaster(SimpleCoordStore pos)
	{
		blockPos = pos;
	}

	@Override
	public double getMana()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMaxMana()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean useMana(double amount, boolean sim)
	{
		return true;
	}

	public SimpleDoubleCoordStore getSpellCreationPos()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setVelocity(EntitySpellProjectile sp)
	{
		// TODO Auto-generated method stub

	}

	public ForgeDirection getFacing()
	{
		return ForgeDirection.NORTH;
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
				spell.apply(this, hitPos);
				return true;
			}
		}
		else
		{
			SimpleDoubleCoordStore cent = blockPos.getCenter();
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
		spell.apply(this, blockPos);
	}

	private boolean doCast(Spell spell)
	{
		double cost = spell.getCost(this);
		SpellPreCastEvent spce = new SpellPreCastEvent(spell, this, cost);
		if(!spce.isCanceled())
			return (useMana(spce.getCost(), false));
		return false;
	}

	@Override
	public void cast(Spell spell)
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
	public void cast(Spell spell, SimpleCoordStore block)
	{
		if(block == null) return;
		if((spell.type == CastType.SELF) && !(block.equals(blockPos))) return;
		if(doCast(spell))
			spell.apply(this, block);
	}

	@Override
	public void cast(Spell spell, Entity ent)
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
}
