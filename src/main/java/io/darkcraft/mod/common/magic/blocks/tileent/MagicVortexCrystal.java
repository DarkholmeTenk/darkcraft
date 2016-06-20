package io.darkcraft.mod.common.magic.blocks.tileent;

import io.darkcraft.darkcore.mod.datastore.Pair;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.interfaces.IActivatable;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.abstracts.AbstractMFTileEntity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.registry.GameData;

public class MagicVortexCrystal extends AbstractMFTileEntity implements IActivatable
{
	private static final double[][] possibleCols = new double[][]{{1,0,0},{0,1,0},{0,0,1},{1,1,0},{1,0,1},{1,0.5,0}};
	private ItemStack is = new ItemStack(Items.diamond,3);
	private EntityItem ei;

	public double[] cols;
	private SimpleCoordStore vortexPos;

	{
		cols = possibleCols[DarkcraftMod.modRand.nextInt(possibleCols.length)];
		is = getRandomIS();
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
		super.readFromNBT(nbt);
		vortexPos = SimpleCoordStore.readFromNBT(nbt,"vpos");
		if((vortexPos != null) && init)
			init();
	}

	@Override
	public void readTransmittable(NBTTagCompound nbt)
	{
		is = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("is"));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		if(vortexPos != null) vortexPos.writeToNBT(nbt,"vpos");
	}

	@Override
	public void writeTransmittable(NBTTagCompound nbt)
	{
		NBTTagCompound inbt = new NBTTagCompound();
		is.writeToNBT(inbt);
		nbt.setTag("is", inbt);
	}

	public Entity getISEntity()
	{
		if((ei == null) || (ei.getEntityItem() != is)) ei = new EntityItem(worldObj,xCoord,yCoord,zCoord,is);
		return ei;
	}

	private static List<Pair<ItemStack,Integer>> spawnData = new ArrayList();
	private static int maxChance;
	public static void setSpawnData(String spawnDataStr)
	{
		spawnData.clear();
		String[] items = spawnDataStr.split(",");
		int totalChance = 0;
		for(String item : items)
		{
			if(item.isEmpty()) continue;
			Item i = null;
			int c = 1;
			int m = 0;
			int w = 1;
			String[] itemData = item.split("#");
			i = GameData.getItemRegistry().getObject(itemData[0]);
			if(i == null)
			{
				System.err.println("Item not found: "+itemData[0]);
				continue;
			}
			if(itemData.length > 1)
				c = MathHelper.toInt(itemData[1], 1);
			if(itemData.length > 2)
				m = MathHelper.toInt(itemData[2], 0);
			if(itemData.length > 3)
				w = MathHelper.toInt(itemData[3], 1);
			Pair<ItemStack,Integer> p = new Pair(new ItemStack(i,c,m),w);
			spawnData.add(p);
			totalChance += w;
			System.out.println("Adding " + p);
		}
		maxChance = totalChance;
		if(spawnData.size() == 0)
			spawnData.add(new Pair(new ItemStack(Items.diamond,1),1));
	}

	public static ItemStack getRandomIS()
	{
		int c = DarkcraftMod.modRand.nextInt(maxChance);
		for(Pair<ItemStack,Integer> data : spawnData)
		{
			c-= data.b;
			if(c <= 0)
				return data.a.copy();
		}
		return spawnData.get(0).a.copy();
	}

	@Override
	public boolean activate(EntityPlayer ent, int side)
	{
		if(ServerHelper.isServer())
		{
			worldObj.spawnEntityInWorld(getISEntity());
			coords().setToAir();
		}
		return true;
	}
}
