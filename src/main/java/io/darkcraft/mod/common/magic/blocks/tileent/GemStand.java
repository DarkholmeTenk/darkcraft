package io.darkcraft.mod.common.magic.blocks.tileent;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import io.darkcraft.darkcore.mod.abstracts.AbstractTileEntity;
import io.darkcraft.darkcore.mod.helpers.MessageHelper;
import io.darkcraft.darkcore.mod.helpers.PlayerHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.darkcore.mod.interfaces.IActivatable;
import io.darkcraft.mod.common.magic.items.SoulGem;
import io.darkcraft.mod.common.magic.items.SoulGem.Size;

public class GemStand extends AbstractTileEntity implements IActivatable
{
	private ItemStack is;
	private String locker;
	private Size soulsize;

	@Override
	public void init()
	{
	}

	@Override
	public void tick()
	{
	}

	public Size getGemSize()
	{
		return SoulGem.getGemSize(is);
	}

	public Size getSoulSize()
	{
		return soulsize;
	}

	private void setGem(ItemStack newIS)
	{
		if(newIS == is) return;
		is = newIS;
		soulsize = SoulGem.getSoulSize(newIS);
		sendUpdate();
	}

	private void activate(EntityPlayer ent)
	{
		if(ServerHelper.isClient()) return;
		ItemStack held = ent.getCurrentEquippedItem();
		if((is != null) && (held == null))
		{
			WorldHelper.giveItemStack(ent, is);
			setGem(null);
		}
		else if((is == null) && (held != null))
		{
			if(SoulGem.getGemSize(held) != null)
				setGem(ent.inventory.decrStackSize(ent.inventory.currentItem,1));
		}
	}

	@Override
	public boolean activate(EntityPlayer ent, int side)
	{
		if(locker != null)
		{
			String un = PlayerHelper.getUsername(ent);
			if(locker.equals(un))
				activate(ent);
			else
				MessageHelper.sendMessage(ent, "dc.message.gemstand.locked");
		}
		else
		{
			if(ent.isSneaking())
			{
				locker = PlayerHelper.getUsername(ent);
				MessageHelper.sendMessage(ent, "dc.mess.gemstand.lock");
			}
			else
				activate(ent);
		}
		return true;
	}

	public boolean isGemFull()
	{
		return soulsize != null;
	}

	@Override
	public void writeTransmittable(NBTTagCompound nbt)
	{
		if(is != null)
		{
			NBTTagCompound isNBT = new NBTTagCompound();
			is.writeToNBT(isNBT);
			nbt.setTag("is", isNBT);
		}
	}

	@Override
	public void readTransmittable(NBTTagCompound nbt)
	{
		if(!nbt.hasKey("is"))
			setGem(null);
		setGem(ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("is")));
	}

	public ItemStack getIS()
	{
		return is;
	}

	@Override
	public void addDrops(List<ItemStack> items)
	{
		if(is != null)
			items.add(is);
	}
}
