package io.darkcraft.mod.common.magic.tileent;

import io.darkcraft.api.magic.IMagicAnvilRecipe;
import io.darkcraft.api.magic.ISpellable;
import io.darkcraft.darkcore.mod.abstracts.AbstractTileEntity;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.helpers.NBTHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.darkcore.mod.interfaces.IActivatablePrecise;
import io.darkcraft.mod.common.magic.caster.ICaster;
import io.darkcraft.mod.common.magic.spell.Spell;
import io.darkcraft.mod.common.registries.MagicAnvilRecipeRegistry;

import java.util.Set;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class MagicAnvil extends AbstractTileEntity implements ISpellable, IActivatablePrecise
{
	private ItemStack[] items = new ItemStack[3];
	private ItemStack[] newItems = new ItemStack[3];
	public EntityItem[] eiArr = new EntityItem[3];
	private IMagicAnvilRecipe rec;
	private ICaster cas;
	private boolean processing = false;
	private int processTime = 0;

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
		return processing;
	}

	@Override
	public boolean spellHit(SimpleCoordStore block, Spell spell, ICaster caster)
	{
		Set<IMagicAnvilRecipe> recipes = MagicAnvilRecipeRegistry.getRecipes();
		for(IMagicAnvilRecipe r : recipes)
		{
			if(r.isValid(this, items, spell))
			{
				ItemStack[] nItems = r.craft(this, caster, items, spell);
				if((nItems == items) || (nItems == null)) continue;
				for(int i = 0; i < 3; i++) newItems[i] = nItems[i];
				processTime = 20;
				processing = true;
				rec = r;
				cas = caster;
				sendUpdate();
				return true;
			}
		}
		return false;
	}

	private void handleActivate(EntityPlayer pl, int slot)
	{
		if(!processing)
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
		if(processing)
		{
			processTime--;
			if(processTime <= 0)
			{
				for(int i = 0; i < 3; i++)
					items[i] = newItems[i];
				if(rec != null)
					rec.craftingDone(this, cas);
				processing = false;
				sendUpdate();
			}
		}
		if(ServerHelper.isClient())
		{
			if(eiArr == null)
			{
				eiArr = new EntityItem[items.length];
				for(int i = 0; i < items.length; i++)
				{
					eiArr[i] = new EntityItem(getWorldObj(), xCoord,yCoord,zCoord, items[i]);
					eiArr[i].hoverStart = 0;
					eiArr[i].rotationYaw = 0;
				}
			}
			for(int i = 0; i < eiArr.length; i++)
				if(eiArr[i] != null)
					eiArr[i].rotationYaw++;
		}
	}

	@Override
	public void writeTransmittable(NBTTagCompound nbt)
	{
		{
			NBTTagCompound iN = new NBTTagCompound();
			NBTHelper.writeItemsToNBT(items, iN);
			nbt.setTag("items", iN);
		}
		{
			NBTTagCompound iN = new NBTTagCompound();
			NBTHelper.writeItemsToNBT(newItems, iN);
			nbt.setTag("newItems", iN);
		}
		nbt.setBoolean("proc", processing);
		nbt.setInteger("process", processTime);
	}

	@Override
	public void readTransmittable(NBTTagCompound nbt)
	{
		NBTHelper.readItemsFromNBT(newItems, nbt.getCompoundTag("newItems"));
		processing = nbt.getBoolean("proc");
		processTime = nbt.getInteger("process");

		ItemStack[] temp = new ItemStack[3];
		NBTHelper.readItemsFromNBT(temp, nbt.getCompoundTag("items"));
		if(ServerHelper.isClient() && (getWorldObj() != null))
			for(int i = 0; i < 3; i++)
			{
				if(temp[i] != null)
				{
					if((items[i] == null) || !WorldHelper.sameItem(items[i], temp[i]))
					{
						items[i] = temp[i];
						eiArr[i] = new EntityItem(getWorldObj(), xCoord,yCoord,zCoord, items[i]);
						eiArr[i].hoverStart = 0;
						eiArr[i].rotationYaw = 0;
					}
				}
				else
				{
					items[i] = null;
					eiArr[i] = null;
				}
			}
		else
			eiArr = null;
	}
}
