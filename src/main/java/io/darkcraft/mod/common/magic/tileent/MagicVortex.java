package io.darkcraft.mod.common.magic.tileent;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.helpers.BlockIterator;
import io.darkcraft.darkcore.mod.interfaces.IBlockIteratorCondition;
import io.darkcraft.mod.abstracts.AbstractMFTileEntity;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MagicVortex extends AbstractMFTileEntity
{
	private ArrayList<SimpleCoordStore> crystals = new ArrayList<SimpleCoordStore>();
	private boolean inLava = false;

	private static boolean isLava(SimpleCoordStore scs)
	{
		Block b = scs.getBlock();
		if(b == null) return false;
		return (b == Blocks.lava) || (b == Blocks.flowing_lava);
	}

	private boolean inLavaLake()
	{
		SimpleCoordStore below = coords().getNearby(ForgeDirection.DOWN);
		if(!isLava(below))
		{
			System.out.println("Not above lava " + coords + "-" + below);
			return false;
		}
		BlockIterator bi = new BlockIterator(below,LavaLakeIterator.i, false, 25);
		while(bi.hasNext())
		{
			SimpleCoordStore scs = bi.next();
			if((scs.getBlock() == Blocks.air) || (scs.getBlock() == null))
			{
				System.out.println("Failed on " + scs +" from " + below);
				return false;
			}
		}
		return true;
	}

	private void recheckLava()
	{
		long n = System.nanoTime();

		int c = 0;
		SimpleCoordStore below = coords().getNearby(ForgeDirection.DOWN);
		below.setBlock(Blocks.lava, 0, 3);
		BlockIterator bi = new BlockIterator(below,LavaLakeIterator.i, false, 25);
		while(bi.hasNext())
		{
			c++;
			SimpleCoordStore scs = bi.next();
			Block b = scs.getBlock();
			if((b == Blocks.lava) && (scs.getMetadata() == 0)) continue;
			if(b == Blocks.grass) continue;
			if((scs.x == 238) && (scs.z == 116))
				System.out.print("");
			if((b == null) || (b == Blocks.air) || (b == Blocks.flowing_lava) || (b == Blocks.obsidian) || ((b == Blocks.lava) && (scs.getMetadata() != 0)))
			{
				scs.setBlock(Blocks.lava, 0, 7);
				scs.notifyBlock();
			}
		}

		System.out.println("Taken: " + ((System.nanoTime() - n)/1000000.0) + "mss for " + c + " blocks");
	}

	@Override
	public void init()
	{
		if(!inLava)
		{
			boolean inL = inLavaLake();
			inLava = inL;
		}
	}

	@Override
	public void tick()
	{
		if(inLava && ((tt % 200) == 0)) recheckLava();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		for(int i = 0; i < crystals.size(); i++)
			crystals.get(i).writeToNBT(nbt,"c"+i);
		nbt.setBoolean("inLava", inLava);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		crystals.clear();
		for(int i = 0; i < 25; i++)
		{
			SimpleCoordStore scs = SimpleCoordStore.readFromNBT(nbt, "c"+i);
			if(scs == null) break;
			TileEntity te = scs.getTileEntity();
			if(te instanceof MagicVortexCrystal)
				crystals.add(scs);
		}
		inLava = nbt.getBoolean("inLava");
	}

	public void addCrystal(SimpleCoordStore coords)
	{

	}

	@Override
	@SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
		return AxisAlignedBB.getBoundingBox(xCoord-8, yCoord, zCoord-8, xCoord+9, yCoord+15, zCoord+9);
    }
}

class LavaLakeIterator implements IBlockIteratorCondition
{
	public static LavaLakeIterator i = new LavaLakeIterator();
	@Override
	public boolean isValid(SimpleCoordStore start, SimpleCoordStore prevPosition, SimpleCoordStore newPosition)
	{
		if(start.y != newPosition.y) return false;
		if((prevPosition.getBlock() == Blocks.lava) || (prevPosition.getBlock() == Blocks.flowing_lava)) return true;
		return false;
	}

}
