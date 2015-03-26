package io.darkcraft.darkcraft.mod.common.spellsystem.components.shapes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcraft.mod.common.spellsystem.PlayerMagicHelper;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellModifier;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellShape;

public class Target implements ISpellShape
{
	private static final double dist = 0.75;

	@Override
	public double getCostCoefficient(int exponent)
	{
		switch(exponent)
		{
			case 1 : return 0.25;
			default : return 0;
		}
	}

	@Override
	public Set<SimpleDoubleCoordStore> getNewLocations(SimpleDoubleCoordStore originalLocation)
	{
		Set<SimpleDoubleCoordStore> nl = new HashSet<SimpleDoubleCoordStore>();
		nl.add(originalLocation);
		return nl;
	}

	@Override
	public SimpleDoubleCoordStore getLocation(EntityLivingBase ent)
	{
		SimpleDoubleCoordStore aimingAt = PlayerMagicHelper.getAimingAt(ent, 30);
		if(aimingAt != null)
			return PlayerMagicHelper.getAimingAt(ent, 30);
		return null;
	}

	@Override
	public Set<EntityLivingBase> getAffectedEnts(SimpleDoubleCoordStore pos)
	{
		HashSet<EntityLivingBase> ents = new HashSet<EntityLivingBase>();
		if(pos == null)
			return ents;
		World w = pos.getWorldObj();
		List possibleEnts = w.getEntitiesWithinAABBExcludingEntity(null, pos.getAABB(dist));
		for(Object o : possibleEnts)
		{
			System.out.println("::"+o.toString());
			if(o instanceof EntityLivingBase)
			{
				System.out.println("E:"+pos.distance((EntityLivingBase)o));
				if(pos.distance((EntityLivingBase)o) < dist)
					ents.add((EntityLivingBase)o);
			}
		}
		return ents;
	}

	@Override
	public Set<SimpleCoordStore> getAffectedBlocks(SimpleDoubleCoordStore pos)
	{
		HashSet<SimpleCoordStore> blocks = new HashSet<SimpleCoordStore>();
		SimpleCoordStore block = new SimpleCoordStore(pos);
		World w = block.getWorldObj();
		if(!w.isAirBlock(block.x, block.y, block.z))
			blocks.add(block);
		else
		{
			if(pos.x == block.x)
				blocks.add(new SimpleCoordStore(block.world,block.x-1,block.y,block.z));
			if(pos.y == block.y)
				blocks.add(new SimpleCoordStore(block.world,block.x,block.y-1,block.z));
			if(pos.z == block.z)
				blocks.add(new SimpleCoordStore(block.world,block.x,block.y,block.z-1));
		}
		return blocks;
	}

	@Override
	public void applyModifiers(Set<ISpellModifier> modifiers)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public int getDuration()
	{
		return 1;
	}

	@Override
	public String getID()
	{
		return "tar";
	}

	@Override
	public ISpellShape create()
	{
		return new Target();
	}

}
