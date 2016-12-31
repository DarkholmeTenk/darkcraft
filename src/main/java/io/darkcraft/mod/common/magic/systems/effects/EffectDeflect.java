package io.darkcraft.mod.common.magic.systems.effects;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;

public class EffectDeflect extends AbstractDarkcraftEffect
{
	private final static List<Entity> eList = new ArrayList<>();

	public EffectDeflect(ICaster _caster, Entity ent, int _magnitude, int _duration)
	{
		super("deflect", _caster, ent, _magnitude, _duration, true, true, 1);
	}

	private List<Entity> getProjectiles()
	{
		Entity ent = getEntity();
		//if(ServerHelper.isClient(ent)) return eList;
		if((ent == null) || (ent.worldObj == null)) return eList;
		List<Entity> entities = new ArrayList<>();
		World w = ent.worldObj;
		SimpleDoubleCoordStore ePos = new SimpleDoubleCoordStore(ent);
		AxisAlignedBB aabb = ePos.getAABB(0).expand(ent.width, ent.width, ent.height);
		aabb = aabb.expand(8, 8, 8);
		List<Entity> wents = w.getEntitiesWithinAABBExcludingEntity(ent, aabb);
		double minDist = Math.max(ent.width, ent.height) + 0.2;
		for(Entity e : wents)
		{
			if(e.isDead || ((e.motionX == 0) && (e.motionY == 0) && (e.motionZ == 0))) continue;
			if((e instanceof IProjectile) || (e instanceof EntityFireball))
			{
				SimpleDoubleCoordStore projPos = new SimpleDoubleCoordStore(e);
				SimpleDoubleCoordStore movePos = projPos.translate(e.motionX, e.motionY, e.motionZ);
				double newDist = movePos.distance(ePos);
				if((newDist <= minDist) && (newDist < projPos.distance(ePos)))
					entities.add(e);
			}
		}
		return entities;
	}

	@Override
	public void apply()
	{
		List<Entity> projectiles = getProjectiles();
		for(Entity e : projectiles)
			e.setDead();
	}

	private final UVStore uv = new UVStore(0.1,0.2,0.3,0.4);
	@Override
	public UVStore getIconLocation(){return uv;}

}
