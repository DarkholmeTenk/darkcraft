package io.darkcraft.mod.common.magic.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import io.darkcraft.darkcore.mod.abstracts.AbstractItem;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.renderer.item.MagicChalkRenderer;
import io.darkcraft.mod.common.magic.systems.symbolic.ChalkType;

import cpw.mods.fml.common.registry.GameRegistry;

public class MagicChalk extends AbstractItem
{
	public MagicChalk()
	{
		super(DarkcraftMod.modName);
		setUnlocalizedName("MagicChalk");
		setSubNames(ChalkType.getNames());
		setMaxStackSize(1);
	}

	@Override
	public void initRecipes()
	{
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(this,1), "dustGlowstone", "dyeWhite", "dyeWhite"));
	}

	@Override
	public void addInfo(ItemStack is, EntityPlayer player, List infoList)
	{
		String s = getString(is);
		if((s == null) || s.isEmpty()) return;
		int slot = getSlot(is);
		String n = "Text: " + EnumChatFormatting.RESET;
		if(slot > 0)
			n += s.substring(0, slot);
		n += EnumChatFormatting.BLUE + s.substring(slot, slot+1) + EnumChatFormatting.RESET;
		if(slot < (s.length() - 1))
			n += s.substring(slot+1);
		infoList.add(n);
	}

	@Override
	public boolean onItemUse(ItemStack is, EntityPlayer pl, World w, int x, int y, int z, int s, float i, float j, float k)
    {
		if(ServerHelper.isClient()) return true;
		if(pl.isSneaking())
		{
			pl.openGui(DarkcraftMod.i, 1399, w, x, y, z);
			return true;
		}

		return true;
    }

	@Override
	public ItemStack onItemRightClick(ItemStack is, World w, EntityPlayer pl)
	{
		if(pl.isSneaking())
		{
			pl.openGui(DarkcraftMod.i, 1399, w, 0, 0, 0);
		}
		return is;

	}

	public static boolean damage(ItemStack is)
	{
		if(is.stackTagCompound == null) return false;
		double nd = is.stackTagCompound.getDouble("dam")+0.002;
		is.stackTagCompound.setDouble("dam", nd);
		return nd >= 1;
	}

	public static String getString(ItemStack is)
	{
		if((is == null) || !(is.getItem() instanceof MagicChalk) || (is.stackTagCompound == null)) return "";
		return is.stackTagCompound.getString("chalkText");
	}

	public static int getSlot(ItemStack is)
	{
		if((is == null) || !(is.getItem() instanceof MagicChalk) || (is.stackTagCompound == null)) return 0;
		String d = getString(is);
		int slot = is.stackTagCompound.getInteger("slot");
		if(d == null) return 0;
		return slot % d.length();
	}

	public static Character getChar(ItemStack is)
	{
		if((is == null) || !(is.getItem() instanceof MagicChalk) || (is.stackTagCompound == null)) return null;
		String d = getString(is);
		int slot = is.stackTagCompound.getInteger("slot");
		if(d == null) return null;
		char c = d.charAt(slot % d.length());
		is.stackTagCompound.setInteger("slot", ++slot);
		return c;
	}

	public static void setString(ItemStack is, String string)
	{
		if((is == null) || !(is.getItem() instanceof MagicChalk)) return;
		if(is.stackTagCompound == null)	is.stackTagCompound = new NBTTagCompound();
		if((string == null) || string.isEmpty()) string = ".";
		is.stackTagCompound.setString("chalkText", string);
		is.stackTagCompound.setInteger("slot", 0);
	}

	public static double getISDamage(ItemStack is)
	{
		if((is == null) || !(is.getItem() instanceof MagicChalk)) return 0;
		if(is.stackTagCompound == null) return 0;
		if(!is.stackTagCompound.hasKey("dam")) is.stackTagCompound.setDouble("dam", 0);
		return is.stackTagCompound.getDouble("dam");
	}

	@Override
	public IItemRenderer getRenderer()
	{
		return new MagicChalkRenderer();
	}

}
