package io.darkcraft.mod.common.magic.tileent;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.abstracts.AbstractMFTileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class MagicVortexCrystal extends AbstractMFTileEntity
{
	private static final double[][] possibleCols = new double[][]{{1,0,0},{0,1,0},{0,0,1},{1,1,0},{1,0,1},{1,0.5,0}};
	private ItemStack is = new ItemStack(Items.diamond,3);
	private EntityItem ei;

	public double[] cols;
	private SimpleCoordStore vortexPos;

	{
		cols = possibleCols[DarkcraftMod.modRand.nextInt(possibleCols.length)];
	}

	public ItemStack getItemstack()
	{
		return is;
	}

	@Override
	public void init()
	{
		if(vortexPos != null)
		{
			TileEntity te = vortexPos.getTileEntity();
			if(te instanceof MagicVortex)
				((MagicVortex)te).addCrystal(coords());
		}
	}

	public void setVortex(SimpleCoordStore pos)
	{
		vortexPos = pos;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		vortexPos = SimpleCoordStore.readFromNBT(nbt,"vpos");
		is = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("is"));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		if(vortexPos != null) vortexPos.writeToNBT(nbt,"vpos");
		NBTTagCompound inbt = new NBTTagCompound();
		is.writeToNBT(inbt);
		nbt.setTag("is", inbt);
	}

	public Entity getISEntity()
	{
		if((ei == null) || (ei.getEntityItem() != is)) ei = new EntityItem(worldObj,xCoord,yCoord,zCoord,is);
		return ei;
	}
}
