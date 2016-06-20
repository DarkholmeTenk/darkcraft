package io.darkcraft.mod.common.magic.systems.soulspell.impl;

import java.util.List;

import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.mod.common.magic.blocks.tileent.GemStand;
import io.darkcraft.mod.common.magic.systems.soulspell.ISoulSpell;
import io.darkcraft.mod.common.magic.systems.spell.caster.BlockCaster;
import io.darkcraft.mod.common.registries.MagicConfig;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class TargetEntsAbstractSoulSpell implements ISoulSpell
{
	private boolean canReach(SimpleDoubleCoordStore cent, Entity ent)
	{
		World w = cent.getWorldObj();
		Vec3 endPos = Vec3.createVectorHelper(ent.posX, ent.posY+ent.getEyeHeight(), ent.posZ);
		for(ForgeDirection fd : ForgeDirection.VALID_DIRECTIONS)
		{
			double x = cent.x + (fd.offsetX*0.5);
			double y = cent.y + (fd.offsetY*0.5);
			double z = cent.z + (fd.offsetZ*0.5);
			Vec3 s = Vec3.createVectorHelper(x, y, z);
			if(w.rayTraceBlocks(s, endPos, MagicConfig.traceLiquids) == null)
				return true;
		}
		return false;
	}

	public Entity getNearestEntity(BlockCaster caster, GemStand stand)
	{
		AxisAlignedBB aabb = caster.blockCenter.getAABB(20);
		World w = stand.getWorldObj();
		List<Object> list = w.getEntitiesWithinAABB(Entity.class, aabb);

		double closestD = Integer.MAX_VALUE;
		Entity closest = null;
		for(Object o : list)
		{
			if((o == null) || !(o instanceof Entity)) continue;
			Entity ent = (Entity) o;
			if(!shouldTarget(ent)) continue;
			double d = caster.blockCenter.distance(ent);
			if(d < closestD)
				if(canReach(caster.blockCenter,ent))
				{
					closestD = d;
					closest = ent;
				}
		}
		return closest;
	}

	public abstract boolean shouldTarget(Entity e);

}
