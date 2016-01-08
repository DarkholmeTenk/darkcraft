package io.darkcraft.mod.common.magic.field;

import io.darkcraft.darkcore.mod.abstracts.AbstractWorldDataStore;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.mod.common.registries.MagicConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class MagicField extends AbstractWorldDataStore
{
	protected final ConcurrentHashMap<SimpleCoordStore, Double> field = new ConcurrentHashMap();

	private static final ForgeDirection[] normals = new ForgeDirection[]{ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.EAST, ForgeDirection.WEST};
	private static ForgeDirection[] validDirections()
	{
		if(MagicConfig.fieldSizeY)
			return ForgeDirection.VALID_DIRECTIONS;
		else
			return normals;
	}

	public MagicField(String s)
	{
		super(s);
	}

	public MagicField(int dim)
	{
		super("DCMF", dim);
	}

	private SimpleCoordStore getInternalPosition(int x, int y, int z)
	{
		int shift = MagicConfig.fieldSize;
		return new SimpleCoordStore(dimID, x >> shift, MagicConfig.fieldSizeY ? y >> shift : 0, z >> shift);
	}

	private double getInternalFieldStrength(SimpleCoordStore start, SimpleCoordStore end, int depth)
	{
		double ratio = 0.8;
		double max = 1.5;
		double value = getInternalFieldStrength(end, false);
		if(depth >= 4)
			return value * ratio;
		int i = 0;
		double x = 0;
		for(ForgeDirection d : validDirections())
		{
			SimpleCoordStore newPos = end.getNearby(d);
			if(newPos.equals(start))
				continue;
			i++;
			x += getInternalFieldStrength(end,newPos,depth+1);
		}
		return max * ((value * ratio) + ((x / i) * (1 - ratio)));
	}

	private double getInternalFieldStrength(SimpleCoordStore pos, boolean average)
	{
		if(!average)
		{
			if(field.containsKey(pos))
				return field.get(pos);
			return 0;
		}
		else
		{
			double value = getInternalFieldStrength(null, pos, 1);
			return Math.min(value, MagicConfig.fieldMax);
		}
	}

	public double getFieldStrength(int x, int y, int z)
	{
		return getInternalFieldStrength(getInternalPosition(x,y,z), true);
	}

	public double getFieldStrength(SimpleCoordStore pos)
	{
		if(pos.world != dimID)
			throw new RuntimeException("Wrong dimension for field strength get /"+pos.world+"/"+dimID);
		return getFieldStrength(pos.x, pos.y, pos.z);
	}

	public void addToFieldStrength(int x, int y, int z, double toAdd)
	{
		SimpleCoordStore internalPos = getInternalPosition(x,y,z);
		double v;
		if(field.containsKey(internalPos))
			v = field.get(internalPos);
		else
			v = 0;
		v += toAdd;
		v = Math.min(v, MagicConfig.fieldMax);
		field.put(internalPos, v);
		markDirty();
	}

	public void addToFieldStrength(SimpleCoordStore pos, double toAdd)
	{
		if(pos.world != dimID)
			throw new RuntimeException("Wrong dimension for field strength get /"+pos.world+"/"+dimID);
		addToFieldStrength(pos.x, pos.y, pos.z, toAdd);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		int i = 0;
		while(nbt.hasKey("fk"+i))
		{
			SimpleCoordStore key = SimpleCoordStore.readFromNBT(nbt, "fk"+i);
			Double val = nbt.getDouble("fv"+i);
			field.put(key, val);
			i++;
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		int i = 0;
		for(Map.Entry<SimpleCoordStore, Double> entry : field.entrySet())
		{
			SimpleCoordStore coord = entry.getKey();
			Double val = entry.getValue();
			coord.writeToNBT(nbt, "fk"+i);
			nbt.setDouble("fv"+i, val);
			i++;
		}
	}

	protected int getSize()
	{
		synchronized(field)
		{
			return field.size();
		}
	}
}
