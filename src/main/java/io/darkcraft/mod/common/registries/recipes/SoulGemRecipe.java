package io.darkcraft.mod.common.registries.recipes;

import io.darkcraft.api.magic.IMagicAnvilRecipe;
import io.darkcraft.darkcore.mod.helpers.OreDictionaryHelper;
import io.darkcraft.mod.common.magic.caster.ICaster;
import io.darkcraft.mod.common.magic.component.IComponent;
import io.darkcraft.mod.common.magic.items.SoulGem;
import io.darkcraft.mod.common.magic.spell.ComponentInstance;
import io.darkcraft.mod.common.magic.spell.Spell;
import io.darkcraft.mod.common.magic.tileent.MagicAnvil;
import io.darkcraft.mod.common.registries.MagicalRegistry;
import net.minecraft.item.ItemStack;

public class SoulGemRecipe implements IMagicAnvilRecipe
{
	public static SoulGemRecipe[] recipes = new SoulGemRecipe[]{
		new SoulGemRecipe("blockGlass",SoulGem.Size.Petty),
		new SoulGemRecipe("blockCoal",SoulGem.Size.Lesser),
		new SoulGemRecipe("gemQuartz",SoulGem.Size.Common),
		new SoulGemRecipe("gemDiamond",SoulGem.Size.Greater),
		new SoulGemRecipe("gemEmerald",SoulGem.Size.Grand)
	};

	private final String item;
	private final SoulGem.Size size;
	private final ItemStack[] desired;
	private final ItemStack[] expected;
	private SoulGemRecipe(String ore, SoulGem.Size _size)
	{
		item = ore;
		size = _size;
		desired = new ItemStack[]{null,OreDictionaryHelper.getItemStack(item, 1),null};
		expected = new ItemStack[]{null,SoulGem.getIS(size, 1),null};
	}

	@Override
	public String id()
	{
		return "dc.rec.soulgem."+size.ordinal();
	}

	@Override
	public boolean isValid(MagicAnvil anvil, ItemStack[] input, Spell spell)
	{
		if((input[0] != null) || (input[2] != null)) return false;
		if((input[1] == null) || (input[1].stackSize > 1)) return false;
		if(!OreDictionaryHelper.matches(input[1], item)) return false;
		for(ComponentInstance ci : spell.components)
			if(ci.component == MagicalRegistry.soulTrap) return true;
		return false;
	}

	@Override
	public ItemStack[] craft(MagicAnvil anvil, ICaster caster, ItemStack[] items, Spell spell)
	{
		return new ItemStack[]{null,SoulGem.getIS(size, 1),null};
	}

	@Override
	public void craftingDone(MagicAnvil anvil, ICaster caster){}

	@Override
	public ItemStack[] getDesiredItems(){ return desired; }

	private static IComponent[] comp = new IComponent[]{MagicalRegistry.soulTrap};
	@Override
	public IComponent[] getDesiredComponent(){ return comp; }

	@Override
	public ItemStack[] getExpectedOutput(){ return expected; }

	@Override
	public boolean isHidden()
	{
		return false;
	}

}
