package io.darkcraft.mod.common.magic.systems.symbolic.impl;

import java.util.List;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.blocks.tileent.GemStand;
import io.darkcraft.mod.common.magic.blocks.tileent.MagicSymbol;
import io.darkcraft.mod.common.magic.systems.spell.caster.PlayerCaster;
import io.darkcraft.mod.common.magic.systems.symbolic.ISymbolicSpell;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public abstract class AbstractSpellEnchanterSymbolic implements ISymbolicSpell
{
	protected SimpleCoordStore	center;
	protected SimpleCoordStore	root;
	protected AxisAlignedBB		aabb;

	public AbstractSpellEnchanterSymbolic(SimpleCoordStore _root, SimpleCoordStore _center)
	{
		root = _root;
		center = _center;
		double dist = root.distance(center);
		aabb = center.getCenter().getAABB(0.5).expand(dist, 1, dist);
	}

	private void cancelCircle()
	{
		TileEntity te = root.getTileEntity();
		if(te instanceof MagicSymbol)
		{
			((MagicSymbol) te).cancelCircle();
		}
	}

	private void removeCircle()
	{
		TileEntity te = root.getTileEntity();
		if(te instanceof MagicSymbol)
		{
			((MagicSymbol)te).clearCircle();
		}
	}

	protected EntityPlayer getPlayer()
	{
		World w = center.getWorldObj();
		if(w == null) return null;
		List l = w.getEntitiesWithinAABB(EntityPlayer.class, aabb);
		EntityPlayer pl = null;
		for(Object o : l)
		{
			if(!(o instanceof EntityPlayer)) return null;
			if(pl != null) return null;
			pl = (EntityPlayer) o;
		}
		return pl;
	}

	protected GemStand getStand()
	{
		TileEntity te = center.getTileEntity();
		if(!(te instanceof GemStand)) return null;
		return (GemStand) te;
	}

	public abstract boolean enchant(PlayerCaster pc, EntityPlayer pl, GemStand gs);

	@Override
	public void tick()
	{
		EntityPlayer pl = getPlayer();
		GemStand gs = getStand();
		if((pl == null) || (gs == null)) return;
		PlayerCaster pc = Helper.getPlayerCaster(pl);
		if(enchant(pc,pl,gs))
		{
			if(gs != null)
				gs.removeGem();
			removeCircle();
		}
		else
			cancelCircle();
	}

	@Override
	public void cancel()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void read(NBTTagCompound nbt){}

	@Override
	public void write(NBTTagCompound nbt){}

	@Override
	public void readTrans(NBTTagCompound nbt){}

	@Override
	public void writeTrans(NBTTagCompound nbt){}

}
