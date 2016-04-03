package io.darkcraft.mod.common.registries.recipes;

import io.darkcraft.api.magic.IMagicAnvilRecipe;
import io.darkcraft.mod.common.magic.caster.ICaster;
import io.darkcraft.mod.common.magic.component.IComponent;
import io.darkcraft.mod.common.magic.items.SoulGem;
import io.darkcraft.mod.common.magic.spell.Spell;
import io.darkcraft.mod.common.magic.tileent.MagicAnvil;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ScrollRecipe implements IMagicAnvilRecipe
{

	@Override
	public boolean isValid(MagicAnvil anvil, ItemStack[] input, Spell spell)
	{
		boolean empty,soul,paper;
		empty=soul=paper=false;
		for(ItemStack is : input)
		{
			if(is == null){ empty = true; continue; }
			if(is.stackSize > 1) return false;
			if(is.getItem() == Items.paper) paper = true;
			if(SoulGem.getSoulSize(is) != null) soul = true;
		}
		return empty && soul && paper;
	}

	@Override
	public ItemStack[] craft(MagicAnvil anvil, ICaster caster, ItemStack[] items, Spell spell)
	{
		return null;
	}

	@Override
	public void craftingDone(MagicAnvil anvil, ICaster caster)
	{

	}

	private static ItemStack[] desired = new ItemStack[]{new ItemStack(Items.paper),null,new ItemStack(ItemBlockRegistry.soulGem)};
	@Override
	public ItemStack[] getDesiredItems(){ return desired; }

	@Override
	public IComponent[] getDesiredComponent(){ return null; }

	@Override
	public ItemStack[] getExpectedOutput(){ return null; }

	@Override
	public boolean isHidden()
	{
		return false;
	}

}
