package io.darkcraft.darkcraft.mod.common.spellsystem.components.shapes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.util.ForgeDirection;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcraft.mod.common.spellsystem.components.modifiers.Size;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellComponent;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellModifier;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellShape;

public class Area implements ISpellShape
{
	private int strength = 1;
	public final static double dist = 2;
	@Override
	public String getID()
	{
		return "area";
	}

	@Override
	public ISpellComponent create()
	{
		return new Area();
	}

	@Override
	public double getCostCoefficient(int exponent)
	{
		switch(exponent)
		{
			case 2 : return 0.5;
			case 1 : return 0.25;
			default : return 0;
		}
	}
	
	private double getOff()
	{
		return 0;
		//return DarkcraftMod.modRand.nextDouble() * 0.2;
	}
	
	private double getDist()
	{
		return dist * strength;
	}

	@Override
	public Set<SimpleDoubleCoordStore> getNewLocations(SimpleDoubleCoordStore oL)
	{
		HashSet<SimpleDoubleCoordStore> positions = new HashSet<SimpleDoubleCoordStore>();
		//positions.add(oL);
		for(ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
		{
			if(d == ForgeDirection.DOWN || d == ForgeDirection.UP)
				continue;
			double oX = (getOff() + d.offsetX) * getDist();
			double oY = (getOff() + d.offsetY) * getDist();
			double oZ = (getOff() + d.offsetZ) * getDist();
			positions.add(new SimpleDoubleCoordStore(oL.world,oL.x+oX,oL.y+oY,oL.z+oZ));
		}
		return positions;
	}

	@Override
	public SimpleDoubleCoordStore getLocation(EntityLivingBase ent)
	{
		return new SimpleDoubleCoordStore(ent);
	}

	@Override
	public Set<EntityLivingBase> getAffectedEnts(SimpleDoubleCoordStore pos)
	{
		HashSet<EntityLivingBase> ents = new HashSet<EntityLivingBase>();
		List possibleEnts = pos.getWorldObj().getEntitiesWithinAABBExcludingEntity(null, pos.getAABB(getDist()));
		for(Object o : possibleEnts)
		{
			if(o instanceof EntityLivingBase)
			{
				if(pos.distance((EntityLivingBase)o) <= getDist())
					ents.add((EntityLivingBase)o);
			}
		}
		return ents;
	}

	@Override
	public Set<SimpleCoordStore> getAffectedBlocks(SimpleDoubleCoordStore pos)
	{
		int max = (int)Math.floor(getDist());
		double distSquare = getDist() * getDist() + 1;
		SimpleCoordStore base = pos.floor();
		HashSet<SimpleCoordStore> blocks = new HashSet<SimpleCoordStore>();
		for (int x = -max; x <= max; x++)
			for (int y = -max; y <= max; y++)
				for (int z = -max; z <= max; z++)
					if (((x * x) + (y * y) + (z * z)) <= distSquare)
						blocks.add(new SimpleCoordStore(base.world, x + base.x, y + base.y, z + base.z));
		return blocks;
	}

	@Override
	public void applyModifiers(Set<ISpellModifier> modifiers)
	{
		for(ISpellModifier mod : modifiers)
		{
			if(mod instanceof Size)
				strength = 1 + mod.getStrength();
		}
	}

	@Override
	public int getDuration()
	{
		return 1;
	}

}
