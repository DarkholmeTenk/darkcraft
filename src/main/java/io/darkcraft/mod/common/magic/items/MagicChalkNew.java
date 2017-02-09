package io.darkcraft.mod.common.magic.items;

import java.util.List;

import com.google.common.base.Strings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;

import io.darkcraft.darkcore.mod.abstracts.AbstractItem;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.renderer.item.MagicChalkRenderer;
import io.darkcraft.mod.common.magic.systems.symbolic.ChalkType;

public class MagicChalkNew extends AbstractItem
{
	public MagicChalkNew()
	{
		super(DarkcraftMod.modName);
		setUnlocalizedName("MagicChalkNew");
		setSubNames(ChalkType.getNames());
		setMaxStackSize(1);
	}

	@Override
	public void initRecipes()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public ItemStack onItemRightClick(ItemStack is, World w, EntityPlayer pl)
	{
		if(ServerHelper.isClient(w) && pl.isSneaking())
		{
			pl.openGui(DarkcraftMod.i, 1400, w, 0, 0, 0);
		}
		return is;
	}

	@Override
	public IItemRenderer getRenderer()
	{
		return new MagicChalkRenderer();
	}

	public static boolean damage(ItemStack is)
	{
		if(is.stackTagCompound == null) return false;
		double nd = is.stackTagCompound.getDouble("dam")+0.002;
		is.stackTagCompound.setDouble("dam", nd);
		return nd >= 1;
	}

	public static double getISDamage(ItemStack is)
	{
		if((is == null) || !(is.getItem() instanceof MagicChalkNew)) return 0;
		if(is.stackTagCompound == null) return 0;
		if(!is.stackTagCompound.hasKey("dam")) is.stackTagCompound.setDouble("dam", 0);
		return is.stackTagCompound.getDouble("dam");
	}

	public static String[] getStrings(ItemStack is)
	{
		if((is == null) || !(is.getItem() instanceof MagicChalkNew)) return new String[]{};
		if(is.stackTagCompound == null) return new String[]{};
		return getStrings(is.stackTagCompound);
	}

	public static String[] getStrings(NBTTagCompound nbt)
	{
		int size = nbt.getInteger("size");
		String[] arr = new String[size];
		for(int i = 0; i < size; i++)
			arr[i] = nbt.getString("text"+i);
		return arr;
	}

	public static void setStrings(ItemStack is, String[] values)
	{
		if((is == null) || !(is.getItem() instanceof MagicChalkNew)) return;
		if(is.stackTagCompound == null)
			is.stackTagCompound = new NBTTagCompound();
		is.stackTagCompound.setInteger("size", values.length);
		for(int i = 0; i < values.length; i++)
			is.stackTagCompound.setString("text"+i, Strings.nullToEmpty(values[i]));
	}

	public static void setStrings(NBTTagCompound nbt, String[] values)
	{
		nbt.setInteger("size", values.length);
		for(int i = 0; i < values.length; i++)
			nbt.setString("text"+i, Strings.nullToEmpty(values[i]));
	}

	@Override
	public void addInfo(ItemStack is, EntityPlayer player, List infoList)
	{
		String[] data = getStrings(is);
		if((data == null) || (data.length == 0))
			return;
		for(String s : data)
		{
			if(s.isEmpty()) continue;
			infoList.add(s);
		}
	}
}
