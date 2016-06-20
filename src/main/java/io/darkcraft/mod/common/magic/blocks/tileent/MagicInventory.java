package io.darkcraft.mod.common.magic.blocks.tileent;

import io.darkcraft.api.magic.IStaffable;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.helpers.MessageHelper;
import io.darkcraft.darkcore.mod.helpers.NBTHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.interfaces.IChunkLoader;
import io.darkcraft.mod.abstracts.AbstractMFTileEntity;
import io.darkcraft.mod.common.magic.items.staff.StaffHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

public class MagicInventory extends AbstractMFTileEntity implements IInventory, IChunkLoader, IStaffable
{
	private final int size = 32;
	private SimpleCoordStore otherInventory = null;
	private int lastOtherInvTT = -1;
	private int lastThisInvTT = -1;
	private ItemStack[] items = new ItemStack[size];
	private int lastAccessTT = -1;

	public MagicInventory(){}

	public MagicInventory(World w)
	{
		worldObj = w;
	}

	public MagicInventory getOtherInventory()
	{
		if(ServerHelper.isClient()) return this;
		if(otherInventory == null) return this;
		TileEntity te = otherInventory.getTileEntity();
		if(!(te instanceof MagicInventory))
		{
			otherInventory = null;
			return this;
		}
		if(te == this)
		{
			otherInventory = null;
			return this;
		}
		MagicInventory ote = ((MagicInventory)te);
		if(ote.otherInventory != null)
		{
			if(ote.otherInventory.equals(coords))
			{
				ote.otherInventory = null;
				otherInventory = null;
				return this;
			}
			else
			{
				otherInventory = ote.otherInventory;
				return getOtherInventory();
			}
		}
		MagicInventory oote = ote.getOtherInventory();
		if(ote != oote)
			otherInventory = oote.coords();
		oote.lastAccessTT = oote.tt;
		return oote;
	}

	public boolean isOwnInventory()
	{
		return getOtherInventory() == this;
	}

	@Override
	public int getSizeInventory()
	{
		return size;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		if((slot < 0) || (slot >= size)) return null;
		return getOtherInventory().items[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int num)
	{
		MagicInventory inv = getOtherInventory();
		if(inv.items[slot] == null) return null;
		if(inv.items[slot].stackSize <= 0)
		{
			inv.items[slot] = null;
			return null;
		}
		int count = inv.items[slot].stackSize;
		if(num >= count)
			num = count;
		ItemStack ret = inv.items[slot].splitStack(num);
		if(count == num) items[slot] = null;
		inv.markDirty();
		inv.sendUpdate();
		inv.lastThisInvTT = inv.tt;
		return ret;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack is)
	{
		MagicInventory inv = getOtherInventory();
		inv.items[slot] = is;
		if((inv.items[slot] != null) && (inv.items[slot].stackSize <= 0))
			inv.items[slot] = null;
		inv.markDirty();
		inv.sendUpdate();
		inv.lastThisInvTT = inv.tt;
	}

	@Override
	public String getInventoryName()
	{
		return "darkcraft.magic.inventory.name";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_)
	{
		return false;
	}

	@Override
	public void openInventory(){}

	@Override
	public void closeInventory(){}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack is)
	{
		MagicInventory inv = getOtherInventory();
		return (inv.items[slot] == null) || inv.items[slot].isItemEqual(is);
	}

	@Override
	public boolean staffActivate(EntityPlayer player, StaffHelper helper)
	{
		if(helper == null) return false;
		if(helper.extraNBT.hasKey("magicInvPos"))
		{
			SimpleCoordStore pos = SimpleCoordStore.readFromNBT(helper.extraNBT,"magicInvPos");
			if(pos.equals(coords()) || player.isSneaking())
			{
				MessageHelper.sendMessage(player,"Cleared linking");
				otherInventory = null;
			}
			else
			{
				MessageHelper.sendMessage(player, "Linked to " + pos.toString());
				otherInventory = pos;
			}
			helper.extraNBT.removeTag("magicInvPos");
			return true;
		}
		else if(!player.isSneaking())
		{
			MessageHelper.sendMessage(player, "Linking to " + coords().toString());
			coords().writeToNBT(helper.extraNBT, "magicInvPos");
			return true;
		}
		else if(otherInventory != null)
		{
			MessageHelper.sendMessage(player, "Cleared linking");
			otherInventory = null;
			return true;
		}
		return false;
	}

	@Override
	public boolean shouldChunkload()
	{
		if(isOwnInventory())
		{
			if((lastAccessTT != -1) && (tt < (lastAccessTT + 100)))
				return true;
		}
		return false;
	}

	@Override
	public ChunkCoordIntPair[] loadable()
	{
		return new ChunkCoordIntPair[]{coords().toChunkCoords()};
	}

	@Override
	public void tick()
	{
		if(ServerHelper.isServer())
		{
			MagicInventory inv = getOtherInventory();
			if((inv == this) && (lastOtherInvTT >= 0))
				lastOtherInvTT = -1;
			if((inv != this) && (inv.lastThisInvTT > lastOtherInvTT))
			{
				lastOtherInvTT = inv.tt;
				sendUpdate();
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		NBTHelper.writeItemsToNBT(items, nbt);
		if(otherInventory != null)
			otherInventory.writeToNBT(nbt, "other");
	}

	@Override
	public void writeTransmittableOnly(NBTTagCompound nbt)
	{
		MagicInventory inv = getOtherInventory();
		NBTHelper.writeItemsToNBT(inv.items, nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		NBTHelper.readItemsFromNBT(items, nbt);
		otherInventory = SimpleCoordStore.readFromNBT(nbt, "other");
	}

	@Override
	public void readTransmittableOnly(NBTTagCompound nbt)
	{
		if(ServerHelper.isClient())
			NBTHelper.readItemsFromNBT(items, nbt);
	}

}
