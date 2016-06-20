package io.darkcraft.mod.common.magic.items;

import java.util.List;

import io.darkcraft.api.magic.IMagicAnvilRecipe;
import io.darkcraft.darkcore.mod.abstracts.AbstractItem;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.renderer.item.SoulGemRenderer;
import io.darkcraft.mod.common.magic.systems.spell.caster.BlockCaster;
import io.darkcraft.mod.common.magic.systems.spell.caster.EntityCaster;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import io.darkcraft.mod.common.registries.MagicAnvilRecipeRegistry;
import io.darkcraft.mod.common.registries.MagicConfig;
import io.darkcraft.mod.common.registries.recipes.SoulGemRecipe;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;

public class SoulGem extends AbstractItem
{
	public static SoulGem i;

	public SoulGem()
	{
		super(DarkcraftMod.modName);
		i = this;
		setUnlocalizedName("SoulGem");
		setSubNames(Size.getNames());
	}

	@Override
	public void initRecipes()
	{
		for(IMagicAnvilRecipe rec : SoulGemRecipe.recipes)
			MagicAnvilRecipeRegistry.addRecipe(rec);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(Item i, CreativeTabs ct, List list)
	{
		for(Size s : Size.values())
		{
			list.add(getIS(s,1));
			ItemStack is = getIS(s,1);
			fill(is,null,s);
			list.add(is);
		}
	}

	@Override
	public void addInfo(ItemStack is, EntityPlayer player, List infoList)
	{
		Size s = getSoulSize(is);
		if(s != null)
		{
			String e = getSoulName(is);
			infoList.add("Soul size: " + s.name());
			if((e != null) && !e.isEmpty())
				infoList.add("Captured: " + e);
		}
	}

	public static Size getGemSize(ItemStack is)
	{
		if((is == null) || (is.getItem() != i)) return null;
		int m = is.getItemDamage();
		return Size.values()[m];
	}

	public static Size getSoulSize(ItemStack is)
	{
		if((is == null) || (is.getItem() != i)) return null;
		int max = is.getItemDamage();
		NBTTagCompound nbt = is.stackTagCompound;
		if((nbt == null) || !nbt.hasKey("soulSize")) return null;
		return Size.values()[Math.min(max,nbt.getInteger("soulSize"))];
	}

	public static String getSoulName(ItemStack is)
	{
		if((is == null) || (is.getItem() != i)) return null;
		NBTTagCompound nbt = is.stackTagCompound;
		if((nbt == null) || !nbt.hasKey("soulEnt")) return null;
		return nbt.getString("soulEnt");
	}

	public static ItemStack getIS(Size size, int num)
	{
		return new ItemStack(i,num,size.ordinal());
	}

	public static boolean fill(ICaster cast, EntityLivingBase ent)
	{
		Size entSoulSize = getSoulSize(ent);
		IInventory inv = null;
		if(cast instanceof EntityCaster)
		{
			EntityLivingBase oent = ((EntityCaster)cast).getCaster();
			if(oent instanceof EntityPlayer)
				inv = ((EntityPlayer) oent).inventory;
		}
		else if(cast instanceof BlockCaster)
		{
			TileEntity te = ((BlockCaster)cast).getCoords().floor().getTileEntity();
			if(te instanceof IInventory)
				inv = (IInventory) te;
		}
		if(inv != null)
		{
			for(int i = 0; i < inv.getSizeInventory(); i++)
			{
				ItemStack is = inv.getStackInSlot(i);
				if(getSoulSize(is) != null) continue;
				Size gemSize = getGemSize(is);
				if((gemSize != null) && gemSize.canFit(entSoulSize))
				{
					if(is.stackSize == 1)
						return fill(is,ent,entSoulSize);
					else
					{
						ItemStack newIS = getIS(gemSize, 1);
						boolean r = fill(newIS, ent, entSoulSize);
						if(r)
						{
							inv.decrStackSize(i, 1);
							WorldHelper.transferItemStack(newIS, inv);
							inv.markDirty();
						}
						return r;
					}
				}
			}
		}
		return false;
	}

	private static Size getSoulSize(EntityLivingBase ent)
	{
		if(ent instanceof EntityPlayer)
			return Size.Black;
		Size[] sizes = Size.values();
		for(int i = MagicConfig.soulHealthSizes.length-1; i >=0; i--)
			if(ent.getMaxHealth() >= MagicConfig.soulHealthSizes[i])
				return sizes[i];
		return Size.Petty;
	}

	public static boolean fill(ItemStack is, EntityLivingBase ent, Size size)
	{
		Size sgs = getGemSize(is);
		if((sgs != null) && sgs.canFit(size) && (is.stackSize == 1))
		{
			NBTTagCompound nbt = new NBTTagCompound();
			if(ent != null)
				nbt.setString("soulEnt", ent.getCommandSenderName());
			nbt.setInteger("soulSize", size.ordinal());
			is.stackTagCompound = nbt;
			return true;
		}
		return false;
	}

	public static enum Size
	{
		Petty,
		Lesser,
		Common,
		Greater,
		Grand,
		Black;

		private static String[] names;
		public static String[] getNames()
		{
			if(names == null)
			{
				names = new String[values().length];
				for(int i = 0; i < values().length; i++)
					names[i] = values()[i].name();
			}
			return names;
		}

		public int powerLevel()
		{
			if(this == Black) return Grand.powerLevel();
			return ordinal();
		}

		/**
		 * @param size
		 * @return return true if a soul gem of this size can fit a soul of size
		 */
		public boolean canFit(Size size)
		{
			if(size == null) return false;
			return ordinal() >= size.ordinal();
		}
	}



	@Override
	public IItemRenderer getRenderer()
	{
		return new SoulGemRenderer();
	}
}
