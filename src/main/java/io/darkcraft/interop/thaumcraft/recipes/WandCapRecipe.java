package io.darkcraft.interop.thaumcraft.recipes;

import java.util.ArrayList;

import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.interop.thaumcraft.DarkcraftTC;
import io.darkcraft.mod.common.magic.items.MagicComponent;
import io.darkcraft.mod.common.magic.items.SoulGem;
import io.darkcraft.mod.common.magic.items.SoulGem.Size;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;

public class WandCapRecipe extends InfusionRecipe
{
	private static AspectList getAspectList()
	{
		AspectList al = new AspectList();
		al.add(Aspect.MAGIC, 16);
		al.add(Aspect.ENERGY, 32);
		al.add(Aspect.AURA, 8);
		return al;
	}

	private static ItemStack[] getItemStackArray()
	{
		return new ItemStack[] {
				MagicComponent.Type.Crystal.getIS(1),
				new ItemStack(ItemBlockRegistry.magicLight, 1),
				MagicComponent.Type.Crystal.getIS(1),
				new ItemStack(ItemBlockRegistry.magicLight, 1)
		};
	}

	private static ItemStack getCentralItem()
	{
		ItemStack is = SoulGem.getIS(Size.Greater, 1);
		SoulGem.fill(is, null, Size.Greater);
		return is;
	}

	public WandCapRecipe()
	{
		super("ROD_darkcraft", new ItemStack(DarkcraftTC.rod), 5, getAspectList(), getCentralItem(), getItemStackArray());
	}

	@Override
	public boolean matches(ArrayList<ItemStack> input, ItemStack central, World world, EntityPlayer player)
	{
		if (!ThaumcraftApiHelper.isResearchComplete(ServerHelper.getUsername(player), research)) return false;

		if(!SoulGem.getSoulSize(central).canFit(SoulGem.Size.Greater)) return false;

		ArrayList<ItemStack> ii = new ArrayList<ItemStack>();
		for (ItemStack is : input)
			ii.add(is.copy());

		compLoop:
		for (ItemStack comp : getComponents())
		{
			for (int a = 0; a < ii.size(); a++)
			{
				ItemStack i2 = ii.get(a).copy();
				if (comp.getItemDamage() == OreDictionary.WILDCARD_VALUE)
					i2.setItemDamage(OreDictionary.WILDCARD_VALUE);
				if (areItemStacksEqual(i2, comp, true))
				{
					ii.remove(a);
					continue compLoop;
				}
			}
			return false;
		}
		return ii.size()==0;
	}

}
