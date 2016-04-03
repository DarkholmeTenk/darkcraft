package io.darkcraft.mod.common.magic.items;

import io.darkcraft.api.magic.IMagicAnvilRecipe;
import io.darkcraft.darkcore.mod.abstracts.AbstractItem;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.registries.MagicAnvilRecipeRegistry;
import io.darkcraft.mod.common.registries.recipes.SoulGemRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

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
		return Size.values()[Math.max(max,nbt.getInteger("soulSize"))];
	}

	public static ItemStack getIS(Size size, int num)
	{
		return new ItemStack(i,num,size.ordinal());
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
	}
}
