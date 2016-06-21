package io.darkcraft.mod.common.magic.systems.symbolic.impl;

import java.util.List;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.handlers.EffectHandler;
import io.darkcraft.darkcore.mod.impl.EntityEffectStore;
import io.darkcraft.mod.common.magic.systems.effects.EffectFeatherFall;
import io.darkcraft.mod.common.magic.systems.symbolic.ISymbolicSpell;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class FeatherfallSymbolic implements ISymbolicSpell
{
	private int r;
	private SimpleCoordStore c;
	private AxisAlignedBB aabb;

	public FeatherfallSymbolic(SimpleCoordStore rootRune, SimpleCoordStore center)
	{
		c = center;
		r = (int) center.diagonalParadoxDistance(rootRune);
		double h = 6.0 / r;
		aabb = AxisAlignedBB.getBoundingBox(c.x-r, c.y-0.5, c.z-r, c.x+1+r, c.y+h, c.z+r+1);
	}

	@Override
	public void tick()
	{
		World w = c.getWorldObj();
		if(w == null) return;
		List l = w.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
		for(Object o : l)
		{
			if(!(o instanceof EntityLivingBase)) continue;
			EntityLivingBase ent = (EntityLivingBase) o;
			EntityEffectStore ees = EffectHandler.getEffectStore(ent);
			if(ees == null) continue;
			ees.addEffect(new EffectFeatherFall(null, ent, 4, 10));
		}
	}

	@Override
	public void cancel(){}

	@Override
	public void read(NBTTagCompound nbt)
	{
	}

	@Override
	public void write(NBTTagCompound nbt)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void readTrans(NBTTagCompound nbt)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void writeTrans(NBTTagCompound nbt)
	{
		// TODO Auto-generated method stub

	}

}
