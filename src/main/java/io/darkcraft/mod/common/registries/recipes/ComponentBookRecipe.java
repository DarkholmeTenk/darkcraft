package io.darkcraft.mod.common.registries.recipes;

import io.darkcraft.api.magic.IMagicAnvilRecipe;
import io.darkcraft.darkcore.mod.helpers.MessageHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.mod.common.magic.blocks.tileent.MagicAnvil;
import io.darkcraft.mod.common.magic.items.ComponentBook;
import io.darkcraft.mod.common.magic.items.SoulGem;
import io.darkcraft.mod.common.magic.items.SoulGem.Size;
import io.darkcraft.mod.common.magic.systems.component.IComponent;
import io.darkcraft.mod.common.magic.systems.spell.Spell;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import io.darkcraft.mod.common.magic.systems.spell.caster.PlayerCaster;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ComponentBookRecipe implements IMagicAnvilRecipe
{

	@Override
	public String id()
	{
		return "dc.rec.compBook";
	}

	@Override
	public boolean isValid(MagicAnvil anvil, ItemStack[] input, Spell spell)
	{
		boolean empty,soul,book;
		empty=soul=book=false;
		for(ItemStack is : input)
		{
			if(is == null){ empty = true; continue; }
			if(is.stackSize > 1) return false;
			if(is.getItem() == Items.book) book = true;
			if((SoulGem.getGemSize(is) != null) && SoulGem.getGemSize(is).canFit(Size.Common)) soul = true;
		}
		return empty && soul && book;
	}

	@Override
	public ItemStack[] craft(MagicAnvil anvil, ICaster caster, ItemStack[] items, Spell spell)
	{
		SoulGem.Size s = null;
		for(int i = 0; i < items.length; i++)
			if(SoulGem.getSoulSize(items[i]) != null)
				s = SoulGem.getSoulSize(items[i]);
		if(s == null)
		{
			if((caster instanceof PlayerCaster) && ServerHelper.isServer())
			{
				EntityPlayer pl = ((PlayerCaster)caster).getCaster();
				if(pl != null)
					MessageHelper.sendMessage(pl, MagicAnvil.emptySoulMessage);
			}
			return null;
		}
		if(!s.canFit(Size.Common))
		{
			if((caster instanceof PlayerCaster) && ServerHelper.isServer())
			{
				EntityPlayer pl = ((PlayerCaster)caster).getCaster();
				if(pl != null)
					MessageHelper.sendMessage(pl, MagicAnvil.smallSoulMessage);
			}
			return null;
		}
		ItemStack book = ComponentBook.getIS(spell.mostExpensiveComponent.component);
		return new ItemStack[]{null,book,null};
	}

	@Override
	public void craftingDone(MagicAnvil anvil, ICaster caster)
	{

	}

	private static ItemStack[] desired = new ItemStack[]{new ItemStack(Items.book),null,SoulGem.getIS(SoulGem.Size.Common, 1)};
	@Override
	public ItemStack[] getDesiredItems(){ return desired; }

	@Override
	public IComponent[] getDesiredComponent(){ return null; }

	private static ItemStack[] out = new ItemStack[]{null,new ItemStack(ItemBlockRegistry.compBook,1), null};
	@Override
	public ItemStack[] getExpectedOutput(){ return out; }

	@Override
	public boolean isHidden()
	{
		// TODO Auto-generated method stub
		return false;
	}

}
