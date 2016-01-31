package io.darkcraft.mod.common.magic.items;

import io.darkcraft.darkcore.mod.abstracts.AbstractItem;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class MagicComponent extends AbstractItem
{
	public static enum Type{
		Crystal;

		public ItemStack getIS(int num)
		{
			return new ItemStack(ItemBlockRegistry.magicComponent,num,ordinal());
		}

		private static String[] names;
		public static String[] getNames()
		{
			if(names == null)
			{
				Type[] types = Type.values();
				names = new String[types.length];
				for(int i = 0; i < names.length; i++)
					names[i] = types[i].name();
			}
			return names;
		}
	};

	public MagicComponent()
	{
		super(DarkcraftMod.modName);
		setUnlocalizedName("MagicComponent");
		setSubNames(Type.getNames());
	}

	@Override
	public void initRecipes()
	{
		GameRegistry.addRecipe(new ShapedOreRecipe(Type.Crystal.getIS(1), false, " g ","gdg"," g ",
				'g', "dustGlowstone",
				'd', "gemDiamond"));
	}

}
