package io.darkcraft.mod.common.items.staff;

import io.darkcraft.mod.common.items.staff.parts.StaffPartRegistry;
import io.darkcraft.mod.common.items.staff.parts.bottom.IStaffBottom;
import io.darkcraft.mod.common.items.staff.parts.head.IStaffHead;
import io.darkcraft.mod.common.items.staff.parts.shaft.IStaffShaft;
import io.darkcraft.mod.common.magic.spell.Spell;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;

import java.lang.ref.WeakReference;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

public class ItemStaffHelper
{
	public static final String	nbtIDName	= "isID";

	private final int					id;
	private IStaffBottom				bottom		= StaffPartRegistry.getStaffBottom(null);
	private IStaffShaft					shaft		= StaffPartRegistry.getStaffShaft(null);
	private IStaffHead					head		= StaffPartRegistry.getStaffHead(null);
	private WeakReference<ItemStack>	itemstack;
	public boolean						inited = false;
	private Spell						spell;

	public ItemStaffHelper(int _id)
	{
		id = _id;
	}

	public ItemStack getIS() { return getIS(false); }

	public ItemStack getIS(boolean spawnNew)
	{
		if(itemstack == null)
		{
			if(!spawnNew)
				return null;
			ItemStack is = new ItemStack(ItemBlockRegistry.itemStaff, 1);
			setIS(is);
			return is;
		}
		return itemstack.get();
	}

	public void setIS(ItemStack newIS)
	{
		if(getIS() == newIS)
			return;
		itemstack = new WeakReference<ItemStack>(newIS);
		markDirty();
	}

	public void render()
	{
		GL11.glPushMatrix();
		bottom.render();
		shaft.render();
		head.render();
		GL11.glPopMatrix();
	}

	public void setStaffBottom(IStaffBottom part)	{ if((part==null) || (part == bottom)) return; bottom = part; markDirty(); }
	public void setStaffShaft(IStaffShaft part)		{ if((part==null) || (part == shaft)) return; shaft = part; markDirty(); }
	public void setStaffHead(IStaffHead part)		{ if((part==null) || (part == head)) return; head = part; markDirty(); }

	public void setSpell(Spell newSpell)
	{
		if(spell == newSpell) return;
		spell = newSpell;
		markDirty();
	}

	public Spell getSpell()
	{
		return spell;
	}

	public void markDirty()
	{
		ItemStack is = getIS();
		if(is != null)
		{
			if(is.stackTagCompound == null)
				is.stackTagCompound = new NBTTagCompound();
			writeToNBT(is.stackTagCompound);
		}
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		if (!nbt.hasKey(nbtIDName) || (nbt.getInteger(nbtIDName) != id)) return;
		bottom	= StaffPartRegistry.getStaffBottom(nbt.getString("staffBottom"));
		shaft	= StaffPartRegistry.getStaffShaft(nbt.getString("staffShaft"));
		head	= StaffPartRegistry.getStaffHead(nbt.getString("staffHead"));
		spell	= nbt.hasKey("spell") ? Spell.readFromNBT(nbt.getCompoundTag("spell")) : null;
		inited = true;
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger(nbtIDName, id);
		nbt.setString("staffBottom", bottom.id());
		nbt.setString("staffShaft", shaft.id());
		nbt.setString("staffHead", head.id());
		if(spell != null)
		{
			NBTTagCompound spellTag = new NBTTagCompound();
			spell.writeToNBT(spellTag);
			nbt.setTag("spell", spellTag);
		}
	}

	public void addInfo(List<String> list, EntityPlayer pl)
	{
		if(spell != null)
			spell.addInfo(list, pl);
	}
}
