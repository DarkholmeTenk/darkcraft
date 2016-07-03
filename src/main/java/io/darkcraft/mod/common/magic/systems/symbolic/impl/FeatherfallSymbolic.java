package io.darkcraft.mod.common.magic.systems.symbolic.impl;

import java.util.List;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.handlers.EffectHandler;
import io.darkcraft.darkcore.mod.impl.EntityEffectStore;
import io.darkcraft.mod.common.magic.blocks.tileent.GemStand;
import io.darkcraft.mod.common.magic.systems.effects.EffectFeatherFall;
import io.darkcraft.mod.common.magic.systems.symbolic.ISymbolicSpell;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class FeatherfallSymbolic implements ISymbolicSpell
{
	private int tt;
	private int r;
	private SimpleCoordStore c;
	private AxisAlignedBB aabb;
	private boolean empowered = false;

	public FeatherfallSymbolic(SimpleCoordStore rootRune, SimpleCoordStore center, boolean _empowered)
	{
		c = center;
		r = (int) center.diagonalParadoxDistance(rootRune);
		empowered = _empowered;
		double h = empowered ? Math.max(3.0/r, 0.5) : Math.max(6.0 / r, 1);
		aabb = AxisAlignedBB.getBoundingBox(c.x-r, c.y-0.5, c.z-r, c.x+1+r, c.y+h, c.z+r+1);
	}

	private boolean isEmpowered()
	{
		if(empowered) return true;
		TileEntity te = c.getTileEntity();
		if(te instanceof GemStand)
			return ((GemStand) te).isGemFull();
		return false;
	}

	@Override
	public void tick()
	{
		if((tt % 5) != 0) return;
		boolean up = isEmpowered();
		World w = c.getWorldObj();
		if(w == null) return;
		List l = w.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
		for(Object o : l)
		{
			if(!(o instanceof EntityLivingBase)) continue;
			EntityLivingBase ent = (EntityLivingBase) o;
			EntityEffectStore ees = EffectHandler.getEffectStore(ent);
			if(ees == null) continue;
			ees.addEffect(new EffectFeatherFall(null, ent, up ? 5 : 4, 6));
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
