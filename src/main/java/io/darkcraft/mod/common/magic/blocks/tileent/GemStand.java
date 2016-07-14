package io.darkcraft.mod.common.magic.blocks.tileent;

import io.darkcraft.darkcore.mod.abstracts.AbstractTileEntity;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.helpers.MessageHelper;
import io.darkcraft.darkcore.mod.helpers.PlayerHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.darkcore.mod.interfaces.IActivatable;
import io.darkcraft.mod.common.magic.items.SoulGem;
import io.darkcraft.mod.common.magic.items.SoulGem.Size;
import io.darkcraft.mod.common.magic.systems.soulspell.ISoulSpell;
import io.darkcraft.mod.common.magic.systems.spell.caster.BlockCaster;
import io.darkcraft.mod.common.magic.systems.spell.caster.IBlockCasterHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class GemStand extends AbstractTileEntity implements IActivatable, IBlockCasterHandler
{
	private BlockCaster caster;
	private ItemStack is;
	private String locker;
	private String soulspellID;
	private ISoulSpell soulspell;
	private Size soulsize;

	private double charge;

	private void clear()
	{
		soulspellID = null;
		soulspell = null;
	}

	@Override
	public void init()
	{
		caster = new BlockCaster(coords(), this);
	}

	@Override
	public void tick()
	{
		if((tt % 10) == 0)
		{
			if(is == null )
				clear();
			else
			{
				String ssid = SoulGem.getSoulSpellID(is);
				if((ssid != null) && ((!ssid.equals(soulspellID)) || (soulspell == null)))
				{
					soulspellID = ssid;
					soulspell = SoulGem.getSoulSpell(is);
					soulspell.setGemStand(this);
				}
			}
		}
		if(((tt % 20) == 0) && (soulsize != null))
			charge = Math.min(charge + soulsize.ordinal(), getMaxMana());
		if(soulspell != null)
			soulspell.tick(caster);
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
		if(newIS == null) clear();
		is = newIS;
		soulsize = SoulGem.getSoulSize(newIS);
		if(charge > getMaxMana())
			charge = getMaxMana();
		sendUpdate();
	}

	private void activate(EntityPlayer ent)
	{
		if(ServerHelper.isClient()) return;
		ItemStack held = ent.getCurrentEquippedItem();
		if((is != null) && (held == null))
		{
			clear();
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

	private SimpleDoubleCoordStore target;
	@Override
	public SimpleDoubleCoordStore getProjectileTarget()
	{
		return target;
	}

	public void setProjectileTarget(SimpleDoubleCoordStore pos)
	{
		target = pos;
	}

	private ForgeDirection facing = ForgeDirection.UP;
	@Override
	public ForgeDirection getFacing()
	{
		return facing;
	}

	public void setFacing(ForgeDirection d)
	{
		facing = d;
	}

	@Override
	public double getMaxMana()
	{
		if(soulsize != null)
			return soulsize.getMaxCharge();
		return 0;
	}

	@Override
	public double getMana()
	{
		return charge;
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
		if((soulspell != null) && (soulspellID != null))
		{
			nbt.setString("soulspellID", soulspellID);
			NBTTagCompound soulCompound = SoulGem.getSoulSpellData(is);
			soulspell.writeToNBT(soulCompound);
			//nbt.setTag("soulspellData", soulCompound);
		}
		if(caster != null)
		{
			NBTTagCompound cnbt = new NBTTagCompound();
			caster.writeToNBT(cnbt);
			nbt.setTag("caster", cnbt);
		}
	}

	@Override
	public void readTransmittable(NBTTagCompound nbt)
	{
		if(!nbt.hasKey("is"))
			setGem(null);
		setGem(ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("is")));
		if(nbt.hasKey("soulspellID"))
		{
			String ssi = nbt.getString("soulspellID");
			if(!ssi.equals(soulspellID))
			{
				soulspell = null;
				soulspellID = ssi;
			}
			else if(soulspell != null)
				soulspell.readFromNBT(SoulGem.getSoulSpellData(is));
		}
		else
			clear();
		if(caster != null)
			caster.readFromNBT(nbt.getCompoundTag("caster"));
	}

	public ItemStack getIS()
	{
		return is;
	}

	public void removeGem()
	{
		is = null;
		sendUpdate();
	}
}
