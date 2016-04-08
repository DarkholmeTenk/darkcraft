package io.darkcraft.mod.common.magic.items;

import io.darkcraft.darkcore.mod.abstracts.AbstractItem;
import io.darkcraft.darkcore.mod.helpers.MessageHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.SpellPartRegistry;
import io.darkcraft.mod.common.magic.caster.PlayerCaster;
import io.darkcraft.mod.common.magic.component.IComponent;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;
import io.darkcraft.mod.common.registries.MagicAnvilRecipeRegistry;
import io.darkcraft.mod.common.registries.recipes.ComponentBookRecipe;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ComponentBook extends AbstractItem
{

	public ComponentBook()
	{
		super(DarkcraftMod.modName);
		setUnlocalizedName("ComponentBook");
	}

	@Override
	public void addInfo(ItemStack is, EntityPlayer player, List infoList)
	{
		IComponent c = getComponent(is);
		if(c != null)
		{
			infoList.add("Component: " + StatCollector.translateToLocal(c.getUnlocalisedName()));
		}
		else
			infoList.add("Component: None");
	}

	public static ItemStack getIS(IComponent c)
	{
		ItemStack is = new ItemStack(ItemBlockRegistry.compBook,1);
		setComponent(is,c);
		return is;
	}

	public static IComponent getComponent(ItemStack is)
	{
		if((is == null) || (is.stackTagCompound ==null) || !is.stackTagCompound.hasKey("compID")) return null;
		return SpellPartRegistry.getComponent(is.stackTagCompound.getString("compID"));
	}

	public static void setComponent(ItemStack is, IComponent comp)
	{
		if(is == null) return;
		is.stackTagCompound = new NBTTagCompound();
		is.stackTagCompound.setString("compID", comp.id());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(Item i, CreativeTabs ct, List list)
	{
		list.add(new ItemStack(i,1));
		for(IComponent c : SpellPartRegistry.getAllComponents())
			list.add(getIS(c));
	}

	@Override
	public boolean onItemUse(ItemStack is, EntityPlayer pl, World w, int x, int y, int z, int s, float i, float j, float k)
    {
		if(ServerHelper.isServer())
		{
			rightClick(is, w, pl);
		}
		return true;
    }

	@Override
	public ItemStack onItemRightClick(ItemStack is, World w, EntityPlayer pl)
	{
		rightClick(is, w, pl);
		return is;
	}

	private void rightClick(ItemStack is, World w, EntityPlayer pl)
	{
		IComponent c = getComponent(is);
		if((c != null) && (pl != null))
		{
			PlayerCaster pc = Helper.getPlayerCaster(pl);
			if(pc.getKnownComponents().contains(c)) return;
			pc.learnComponent(c);
			pl.inventory.decrStackSize(pl.inventory.currentItem, 1);
			MessageHelper.sendMessage(pl, c.getIcon(), c.getIconLocation(), "Learnt: " + c.getUnlocalisedName(), 5);
		}
	}

	@Override
	public void initRecipes()
	{
		MagicAnvilRecipeRegistry.addRecipe(new ComponentBookRecipe());
	}

}
