package io.darkcraft.mod.common.magic.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;

import io.darkcraft.darkcore.mod.abstracts.AbstractItem;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.renderer.item.MagicScrollRenderer;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.systems.spell.CastType;
import io.darkcraft.mod.common.magic.systems.spell.Spell;
import io.darkcraft.mod.common.magic.systems.spell.caster.PlayerCaster;
import io.darkcraft.mod.common.registries.MagicAnvilRecipeRegistry;
import io.darkcraft.mod.common.registries.recipes.ScrollRecipe;

public class MagicScroll extends AbstractItem
{
	public MagicScroll()
	{
		super(DarkcraftMod.modName);
		setUnlocalizedName("MagicScroll");
	}

	@Override
	public void initRecipes()
	{
		MagicAnvilRecipeRegistry.addRecipe(new ScrollRecipe());
	}

	@Override
	public void addInfo(ItemStack is, EntityPlayer player, List infoList)
	{
		Spell s = getSpell(is);
		if(s != null)
		{
			infoList.add("Spell: " + s.name);
			infoList.add("Uses: " + getUses(is));
		}
	}

	@Override
	public boolean onItemUse(ItemStack is, EntityPlayer pl, World w, int x, int y, int z, int s, float i, float j, float k)
    {
		if(ServerHelper.isServer(w))
		{
			Spell spell = getSpell(is);
			PlayerCaster caster = Helper.getPlayerCaster(pl);
			if((spell != null) && (caster.getCurrentSpell() != null))
			{
				if(spell.type != CastType.SELF)
					caster.cast(spell, new SimpleCoordStore(w,x,y,z), s, false);
				else
					caster.cast(spell, pl, false);
				decreaseUses(is,pl);
				return true;
			}
			rightClick(is, w, pl);
		}
		return true;
    }

	@Override
	public ItemStack onItemRightClick(ItemStack is, World w, EntityPlayer pl)
	{
		if(ServerHelper.isServer(w))
			rightClick(is, w, pl);
		return is;
	}

	private void rightClick(ItemStack is, World w, EntityPlayer pl)
	{
		PlayerCaster pc = Helper.getPlayerCaster(pl);
		Spell spell = getSpell(is);
		if((pl != null) && (spell != null))
		{
			pc.cast(spell, false);
			decreaseUses(is,pl);
		}
	}

	private int getUses(ItemStack is)
	{
		if((is == null) || (is.stackTagCompound == null) || !is.stackTagCompound.hasKey("uses")) return 0;
		return is.stackTagCompound.getInteger("uses");
	}

	private void decreaseUses(ItemStack is, EntityPlayer pl)
	{
		if((is == null) || (is.stackTagCompound == null) || !is.stackTagCompound.hasKey("uses")) return;
		if((pl == null) || pl.capabilities.isCreativeMode) return;
		int uses = is.stackTagCompound.getInteger("uses");
		if(is.stackSize == 1)
		{
			if(uses <= 1)
				pl.inventory.setInventorySlotContents(pl.inventory.currentItem, null);
			else
				is.stackTagCompound.setInteger("uses", uses-1);
		}
		else
		{
			pl.inventory.decrStackSize(pl.inventory.currentItem, 1);
			if(uses > 1)
			{
				ItemStack newIS = is.copy();
				newIS.stackSize = 1;
				newIS.stackTagCompound.setInteger("uses", uses-1);
				WorldHelper.giveItemStack(pl, newIS);
			}
		}
		pl.inventory.markDirty();
	}

	public static void setSpell(ItemStack is, Spell spell, int uses)
	{
		if((spell == null) || (is == null)) return;
		is.stackTagCompound = new NBTTagCompound();
		NBTTagCompound snbt = new NBTTagCompound();
		spell.writeToNBT(snbt);
		is.stackTagCompound.setInteger("uses", uses);
		is.stackTagCompound.setTag("spell", snbt);
	}

	public static Spell getSpell(ItemStack is)
	{
		if((is == null) || (is.stackTagCompound == null) || !is.stackTagCompound.hasKey("spell")) return null;
		Spell s = Spell.readFromNBT(is.stackTagCompound.getCompoundTag("spell"));
		return s;
	}

	@Override
	public IItemRenderer getRenderer()
	{
		return new MagicScrollRenderer();
	}

}
