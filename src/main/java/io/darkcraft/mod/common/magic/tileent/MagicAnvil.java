package io.darkcraft.mod.common.magic.tileent;

import io.darkcraft.darkcore.mod.abstracts.AbstractTileEntity;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.helpers.NBTHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.darkcore.mod.interfaces.IActivatablePrecise;
import io.darkcraft.mod.common.magic.caster.ICaster;
import io.darkcraft.mod.common.magic.spell.ISpellable;
import io.darkcraft.mod.common.magic.spell.Spell;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class MagicAnvil extends AbstractTileEntity implements ISpellable, IActivatablePrecise
{
	private ItemStack[] items = new ItemStack[3];

	public EntityItem[] eiArr = new EntityItem[3];

	public ItemStack getLeftItem()
	{
		return items[0];
	}

	public ItemStack getCenterItem()
	{
		return items[1];
	}

	public ItemStack getRightItem()
	{
		return items[2];
	}

	public boolean isProcessing()
	{
		return false;
	}

	@Override
	public boolean spellHit(SimpleCoordStore block, Spell spell, ICaster caster)
	{
		// TODO Auto-generated method stub
		return false;
	}

	private void handleActivate(EntityPlayer pl, int slot)
	{
		if(items[slot] == null)
		{
			if(pl.getHeldItem() != null)
			{
				items[slot] = pl.getHeldItem();
				pl.inventory.setInventorySlotContents(pl.inventory.currentItem, null);
				sendUpdate();
			}
		}
		else
		{
			if(pl.getHeldItem() == null)
			{
				WorldHelper.giveItemStack(pl, items[slot]);
				items[slot] = null;
				sendUpdate();
			}
		}
	}

	@Override
	public boolean activate(EntityPlayer ent, int side, float x, float y, float z)
	{
		if((x > 0.25) && (z > 0.25) && (x < 0.75) && (z < 0.75))
		{
			handleActivate(ent, 1);
		}
		else
		{
			float i = (getBlockMetadata() & 1) == 0 ? x : z;
			if(i < 0.5)
			{
				handleActivate(ent, 0);
			}
			else
			{
				handleActivate(ent, 2);
			}
		}
		return true;
	}

	@Override
	public void tick()
	{
		if(ServerHelper.isClient())
		{
			for(int i = 0; i < eiArr.length; i++)
				if(eiArr[i] != null)
					eiArr[i].rotationYaw++;
		}
	}

	@Override
	public void writeTransmittable(NBTTagCompound nbt)
	{
		NBTHelper.writeItemsToNBT(items, nbt);
	}

	@Override
	public void readTransmittable(NBTTagCompound nbt)
	{
		NBTHelper.readItemsFromNBT(items, nbt);
		if(ServerHelper.isClient())
			for(int i = 0; i < 3; i++)
			{
				if(items[i] != null)
				{
					eiArr[i] = new EntityItem(getWorldObj(), xCoord,yCoord,zCoord, items[i]);
					eiArr[i].hoverStart = 0;
					eiArr[i].rotationYaw = 0;
				}
				else
					eiArr[i] = null;
			}
	}
}
